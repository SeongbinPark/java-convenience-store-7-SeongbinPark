package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.Cart;
import store.domain.OrderRequest;
import store.domain.Product;
import store.domain.Promotion;
import store.view.InputView;

public class StoreService {
    private static final Pattern ORDER_PATTERN = Pattern.compile("\\[(.*?)-(\\d+)]");
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final List<Product> products;
    private final PromotionService promotionService;
    private final Cart cart;
    private int completeSets = 0;

    public StoreService(List<Product> products, PromotionService promotionService) {
        this.products = new ArrayList<>(products);
        this.promotionService = promotionService;
        this.cart = new Cart();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    private Product findPromotionProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && p.hasPromotion())
                .findFirst()
                .orElse(null);
    }

    private Product findNormalProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && !p.hasPromotion())
                .findFirst()
                .orElse(null);
    }

    public void processOrder(String orderInput, InputView inputView) {
        cart.clear();
        List<OrderRequest> requests = parseOrder(orderInput);

        for (OrderRequest request : requests) {
            processOrderRequest(request, inputView);
        }
    }

    private List<OrderRequest> parseOrder(String input) {
        List<OrderRequest> requests = new ArrayList<>();
        Matcher matcher = ORDER_PATTERN.matcher(input);

        while (matcher.find()) {
            String productName = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            requests.add(new OrderRequest(productName, quantity));
        }

        if (requests.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 주문 형식입니다.");
        }
        return requests;
    }

    private void processOrderRequest(OrderRequest request, InputView inputView) {
        Product product = findProduct(request.productName());
        int quantity = request.quantity();

        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 1개 이상이어야 합니다.");
        }

        if (quantity > getTotalStockForProduct(product.getName())) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] 재고가 부족합니다. 현재 총 재고: %d개", getTotalStockForProduct(product.getName())));
        }

        if (product.hasPromotion() && promotionService.canApplyPromotion(product)) {
            handlePromotionPurchase(product, quantity, inputView);
        } else {
            // 일반 구매
        }
    }

    private int getTotalStockForProduct(String productName) {
        return 0;
    }

    private Product findProduct(String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."));
    }

    private void handlePromotionPurchase(Product promotionProduct, int quantity, InputView inputView) {
        Promotion promotion = promotionService.getPromotion(promotionProduct.getPromotionType());
        int buyQuantity = promotion.getBuyQuantity();
        int availablePromotionStock = promotionProduct.getPromotionStock();

        // 프로모션 적용 가능한 세트 수 계산
        completeSets = Math.min(availablePromotionStock, quantity) / (buyQuantity + 1);

        int promotionQuantity = completeSets * (buyQuantity + 1);
        // 구매 수량이 정확히 n개(2+1의 경우 2개)일 때만 추가 구매 제안
        if (quantity == buyQuantity && availablePromotionStock >= buyQuantity) {
            if (inputView.readPromotionAddition(promotionProduct)) {
                quantity = buyQuantity + 1;
                completeSets = 1;
            }
        }
        // n의 배수보다 큰 수량을 구매하고, 프로모션 적용이 일부만 가능한 경우에만 메시지 출력
        else if (quantity > promotionQuantity + 1 && promotionQuantity > 0) {
            int nonPromotionQuantity = quantity - promotionQuantity;
            if (!inputView.readNormalPriceConfirmation(promotionProduct, nonPromotionQuantity)) {
                promotionProduct.setPromotionStock(promotionQuantity);
                cart.addOrder(promotionProduct, promotionProduct.getPromotionStock(), completeSets);
                promotionProduct.setPromotionStock(availablePromotionStock);

                // 재고 차감
                System.out.println(promotionQuantity + " " + quantity + " " + availablePromotionStock);

                return;
            }
        }

        int useFromPromotion = 0; // 재고 차감 (무조건 프로모션 재고 우선 사용)

        // 부족한 수량은 일반 재고에서 처리
        int remainingQuantity = quantity - useFromPromotion;
        if (remainingQuantity > 0) {
            Product normalProduct = findNormalProduct(promotionProduct.getName());
            if (normalProduct != null) {
                normalProduct.processOrder(remainingQuantity, 0);
            }
        }

        cart.addOrder(promotionProduct, quantity, completeSets);
    }
}
