package store.domain;

import store.constant.ErrorMessages;

public record OrderRequest(String productName, int quantity) {
    public OrderRequest {
        if (quantity <= 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_QUANTITY.getMessage());
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.EMPTY_PRODUCT_NAME.getMessage());
        }
    }
}
