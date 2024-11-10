// src/main/java/store/domain/OrderItem.java
package store.domain;

public record OrderItem(Product product, int quantity) {
    public int calculateTotalPrice() {
        return product.getPrice() * quantity;
    }
}