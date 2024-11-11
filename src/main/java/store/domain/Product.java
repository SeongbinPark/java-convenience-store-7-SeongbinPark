package store.domain;

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
        if (quantity > getTotalStock()) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] 재고가 부족합니다. 현재 총 재고: %d개", getTotalStock()));
        }

        // 무조건 프로모션 재고 먼저 사용
        final int fromPromotion = Math.min(promotionStock, quantity);
        promotionStock -= fromPromotion;

        // 부족한 만큼만 일반 재고 사용
        final int remainingQuantity = quantity - fromPromotion;
        if (remainingQuantity > 0) {
            normalStock -= remainingQuantity;
        }
    }
}
