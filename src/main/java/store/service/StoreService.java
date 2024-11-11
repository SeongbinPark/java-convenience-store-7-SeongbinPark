package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.Cart;
import store.domain.OrderRequest;
import store.domain.Product;
import store.view.InputView;

public class StoreService {
    private static final Pattern ORDER_PATTERN = Pattern.compile("\\[(.*?)-(\\d+)]");
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final List<Product> products;
    private final PromotionService promotionService;
    private final Cart cart;

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
            // 프로모션 구매
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
}
