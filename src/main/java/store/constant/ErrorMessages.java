package store.constant;

public enum ErrorMessages {
    INVALID_ORDER_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_QUANTITY("[ERROR] 수량은 1개 이상이어야 합니다."),
    EMPTY_PRODUCT_NAME("[ERROR] 상품명은 비어있을 수 없습니다."),
    INSUFFICIENT_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_YES_NO_INPUT("[ERROR] Y 또는 N만 입력 가능합니다. 다시 입력해 주세요."),
    INVALID_PRODUCT_FORMAT("[ERROR] 상품 정보 형식이 올바르지 않습니다."),
    INVALID_PROMOTION_FORMAT("[ERROR] 프로모션 정보 형식이 올바르지 않습니다."),
    INVALID_PRICE_FORMAT("[ERROR] 상품 가격 형식이 올바르지 않습니다."),
    INVALID_QUANTITY_FORMAT("[ERROR] %s는 0보다 작을 수 없습니다."),
    INVALID_BUY_QUANTITY_FORMAT("[ERROR] 구매 수량 형식이 올바르지 않습니다."),
    INVALID_FREE_QUANTITY_FORMAT("[ERROR] 증정 수량 형식이 올바르지 않습니다."),
    INVALID_DATE_FORMAT("[ERROR] %s 형식이 올바르지 않습니다. (YYYY-MM-DD)"),
    PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    ERROR_LOADING_PRODUCTS("[ERROR] 상품 정보를 불러올 수 없습니다."),
    ERROR_LOADING_PROMOTIONS("[ERROR] 프로모션 정보를 불러올 수 없습니다.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
