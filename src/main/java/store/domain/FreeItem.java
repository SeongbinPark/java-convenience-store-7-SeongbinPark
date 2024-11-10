// src/main/java/store/domain/FreeItem.java
package store.domain;

public record FreeItem(Product product, int quantity) {
    public int calculateDiscountAmount() {
        return product.getPrice() * quantity;
    }
}