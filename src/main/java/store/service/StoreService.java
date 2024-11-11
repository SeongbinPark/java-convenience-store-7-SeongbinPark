package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.constant.ErrorMessages;
import store.domain.Cart;
import store.domain.FreeItem;
import store.domain.OrderItem;
import store.domain.OrderRequest;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.view.InputView;

public class StoreService {
    private static final Pattern ORDER_PATTERN = Pattern.compile("\\[(.*?)-(\\d+)]");
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final List<Product> products;
    private final PromotionService promotionService;
    private final Cart cart;
    private int completeSets = 0;

    public StoreService(final List<Product> products, final PromotionService promotionService) {
        this.products = new ArrayList<>(products);
        this.promotionService = promotionService;
        this.cart = new Cart();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    private int getTotalStockForProduct(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .mapToInt(p -> p.getPromotionStock() + p.getNormalStock())
                .sum();
    }

    private Product findPromotionProduct(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && p.hasPromotion())
                .findFirst()
                .orElse(null);
    }

    private Product findNormalProduct(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && !p.hasPromotion())
                .findFirst()
                .orElse(null);
    }

    public void processOrder(final String orderInput, final InputView inputView) {
        cart.clear();
        final List<OrderRequest> requests = parseOrder(orderInput);

        for (final OrderRequest request : requests) {
            processOrderRequest(request, inputView);
        }
    }

    private List<OrderRequest> parseOrder(final String input) {
        final List<OrderRequest> requests = new ArrayList<>();
        final Matcher matcher = ORDER_PATTERN.matcher(input);
        createOrderRequest(matcher, requests);

        if (requests.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_ORDER_FORMAT.getMessage());
        }
        return requests;
    }

    private static void createOrderRequest(Matcher matcher, List<OrderRequest> requests) {
        while (matcher.find()) {
            final String productName = matcher.group(1);
            final int quantity = Integer.parseInt(matcher.group(2));
            requests.add(new OrderRequest(productName, quantity));
        }
    }

    private void processOrderRequest(final OrderRequest request, final InputView inputView) {
        final Product product = findProduct(request.productName());
        int quantity = request.quantity();
        validateQuantity(quantity);
        validateStock(quantity, product);
        if (product.hasPromotion() && promotionService.canApplyPromotion(product)) {
            handlePromotionPurchase(product, quantity, inputView);
            return;
        }
        handleNormalPurchase(product, quantity);
    }

    private void validateStock(int quantity, Product product) {
        if (quantity > getTotalStockForProduct(product.getName())) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessages.INSUFFICIENT_STOCK.getMessage(),
                            getTotalStockForProduct(product.getName())));
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_QUANTITY.getMessage());
        }
    }


    private void handlePromotionPurchase(final Product promotionProduct, int quantity, final InputView inputView) {
        final Promotion promotion = promotionService.getPromotion(promotionProduct.getPromotionType());
        final int buyQuantity = promotion.getBuyQuantity();
        final int availablePromotionStock = promotionProduct.getPromotionStock();
        completeSets = calculateCompleteSets(quantity, availablePromotionStock, buyQuantity); // 프로모션 적용 가능한 세트 수 계산
        final int promotionQuantity = completeSets * (buyQuantity + 1);
        quantity = offerAdditionalPromotion(promotionProduct, quantity, inputView, buyQuantity,
                availablePromotionStock); // 구매 수량이 정확히 n개(2+1의 경우 2개)일 때만 추가 구매 제안
        finalizePromotionPurchase(promotionProduct, quantity, inputView, promotionQuantity, availablePromotionStock);
    }

    private void finalizePromotionPurchase(Product promotionProduct, int quantity, InputView inputView,
                                           int promotionQuantity,
                                           int availablePromotionStock) {
        if (handlePartialPromotionRejection(promotionProduct, quantity, inputView, promotionQuantity,
                availablePromotionStock)) {
            return;
        }
        final int useFromPromotion = reducePromotionStock(promotionProduct, quantity,
                availablePromotionStock); // 재고 차감 (무조건 프로모션 재고 우선 사용)
        final int remainingQuantity = quantity - useFromPromotion; // 부족한 수량은 일반 재고에서 처리
        handleRemainingQuantity(promotionProduct, remainingQuantity);
        cart.addOrder(promotionProduct, quantity, completeSets);
    }

    private static int calculateCompleteSets(int quantity, int availablePromotionStock, int buyQuantity) {
        return Math.min(availablePromotionStock, quantity) / (buyQuantity + 1);
    }

    private int offerAdditionalPromotion(Product promotionProduct, int quantity, InputView inputView, int buyQuantity,
                                         int availablePromotionStock) {
        if (quantity == buyQuantity && availablePromotionStock >= buyQuantity) {
            if (inputView.readPromotionAddition(promotionProduct)) {
                quantity = buyQuantity + 1;
                completeSets = 1;
            }
        }
        return quantity;
    }

    private void handleRemainingQuantity(Product promotionProduct, int remainingQuantity) {
        if (remainingQuantity > 0) {
            final Product normalProduct = findNormalProduct(promotionProduct.getName());
            if (normalProduct != null) {
                normalProduct.processOrder(remainingQuantity, 0);
            }
        }
    }

    private boolean handlePartialPromotionRejection(Product promotionProduct, int quantity, InputView inputView,
                                                    int promotionQuantity,
                                                    int availablePromotionStock) {
        if (quantity > promotionQuantity + 1 && promotionQuantity > 0) {
            final int nonPromotionQuantity = quantity - promotionQuantity;
            if (!inputView.readNormalPriceConfirmation(promotionProduct, nonPromotionQuantity)) {
                processPromotionRejection(promotionProduct, quantity, promotionQuantity, availablePromotionStock);
                return true;
            }
        }
        return false;
    }

    private void processPromotionRejection(Product promotionProduct, int quantity, int promotionQuantity,
                                           int availablePromotionStock) {
        promotionProduct.setPromotionStock(promotionQuantity);
        cart.addOrder(promotionProduct, promotionProduct.getPromotionStock(), completeSets);
        promotionProduct.setPromotionStock(availablePromotionStock);

        reducePromotionStock(promotionProduct, quantity, promotionQuantity);
        System.out.println(promotionQuantity + " " + quantity + " " + availablePromotionStock);
    }


    private static int reducePromotionStock(final Product promotionProduct, final int quantity,
                                            final int availablePromotionStock) {
        final int useFromPromotion = Math.min(quantity, availablePromotionStock);
        promotionProduct.processOrder(useFromPromotion, useFromPromotion);
        return useFromPromotion;
    }


    private void handleNormalPurchase(final Product product, final int quantity) {
        product.processOrder(quantity, 0);
        cart.addOrder(product, quantity, 0);
    }

    private Product findProduct(final String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND.getMessage()));
    }

    public Receipt generateReceipt(final boolean useMembership) {
        final int totalAmount = calculateTotalAmount();
        final int promotionDiscount = calculatePromotionDiscount();
        final int membershipDiscount = calculateMembershipDiscount(totalAmount, promotionDiscount, useMembership);
        return new Receipt(
                cart.getOrderItems(),
                cart.getFreeItems(),
                totalAmount,
                promotionDiscount,
                membershipDiscount
        );
    }

    private int calculateTotalAmount() {
        return cart.getOrderItems().stream()
                .mapToInt(OrderItem::calculateTotalPrice)
                .sum();
    }

    private int calculatePromotionDiscount() {
        return cart.getFreeItems().stream()
                .mapToInt(item -> item.product().getPrice() * item.quantity())
                .sum(); // 증정품 개수만큼만 할인 (각 증정품의 가격 합)
    }

    private int calculateMembershipDiscount(final int totalAmount, final int promotionDiscount,
                                            final boolean useMembership) {
        if (!useMembership) {
            return 0;
        }
        int discountableAmount = totalAmount;
        discountableAmount = calculateTotalPromotionDiscount(discountableAmount);
        final int discountAmount = (int) (Math.max(0, discountableAmount) * MEMBERSHIP_DISCOUNT_RATE);

        return Math.min(discountAmount, MAX_MEMBERSHIP_DISCOUNT);
    }

    private int calculateTotalPromotionDiscount(int discountableAmount) {
        for (final FreeItem freeItem : cart.getFreeItems()) {
            final Product product = freeItem.product();
            final Promotion promotion = promotionService.getPromotion(product.getPromotionType());
            discountableAmount -=
                    (product.getPrice() * (promotion.getBuyQuantity() + promotion.getFreeQuantity())) * completeSets;
        }
        return discountableAmount; // 프로모션이 적용된 금액을 차감 (예: 2+1이면 3개 금액, 1+1이면 2개 금액) -> 2+1에서 6개면 두번 적용
    }
}
