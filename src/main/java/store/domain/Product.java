// src/main/java/store/domain/Product.java
package store.domain;

public class Product {
    private final String name;
    private final int price;
    private int promotionStock;
    private int normalStock;
    private final String promotionType;

    public Product(String name, int price, int promotionStock, int normalStock, String promotionType) {
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

    public void setPromotionStock(int price) {
        promotionStock = price;
    }

    public boolean hasPromotion() {
        return !promotionType.isEmpty() && !"null".equals(promotionType);
    }

}