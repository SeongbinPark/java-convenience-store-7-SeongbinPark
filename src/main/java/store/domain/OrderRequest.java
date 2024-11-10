// src/main/java/store/domain/OrderRequest.java
package store.domain;

public record OrderRequest(String productName, int quantity) {
    public OrderRequest {
        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 1개 이상이어야 합니다.");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품명은 비어있을 수 없습니다.");
        }
    }
}
