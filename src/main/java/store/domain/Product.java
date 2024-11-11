package store.domain;

import store.constant.ErrorMessages;

public class Product {
    private final String name;
    private final int price;
    private int promotionStock;
    private int normalStock;
    private final String promotionType;

    public Product(final String name, final int price, final int promotionStock, final int normalStock,
                   final String promotionType) {
        this.name = name;
        this.price = price;
        this.promotionStock = promotionStock;
        this.normalStock = normalStock;
        this.promotionType = promotionType != null && !"null".equals(promotionType) ? promotionType : "";
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public int getNormalStock() {
        return normalStock;
    }

    public int getTotalStock() {
        return promotionStock + normalStock;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionStock(final int price) {
        promotionStock = price;
    }

    public boolean hasPromotion() {
        return !promotionType.isEmpty() && !"null".equals(promotionType);
    }

    public void processOrder(final int quantity, final int promotionQuantity) {
        ensureSufficientStock(quantity);

        final int fromPromotion = Math.min(promotionStock, quantity); // 무조건 프로모션 재고 먼저 사용
        promotionStock -= fromPromotion;

        final int remainingQuantity = quantity - fromPromotion; // 부족한 만큼만 일반 재고 사용
        if (remainingQuantity > 0) {
            normalStock -= remainingQuantity;
        }
    }

    private void ensureSufficientStock(int quantity) {
        if (quantity > getTotalStock()) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessages.INSUFFICIENT_STOCK.getMessage(), getTotalStock()));
        }
    }
}
