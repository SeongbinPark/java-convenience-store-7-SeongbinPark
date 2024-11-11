package store.constant;

public enum OutputMessages {
    WELCOME("안녕하세요. W편의점입니다."),
    CURRENT_PRODUCTS("현재 보유하고 있는 상품입니다."),
    PRODUCT_AVAILABLE("- %s %,d원 %d개%s%n"),
    PRODUCT_UNAVAILABLE("- %s %,d원 재고 없음%s%n"),
    RECEIPT_HEADER("==============W 편의점================"),
    RECEIPT_SECTION_FREE_ITEMS("=============증\t\t정==============="),
    RECEIPT_SECTION_TOTAL("===================================="),
    TOTAL_PURCHASE("총구매액"),
    PROMOTION_DISCOUNT("행사할인"),
    MEMBERSHIP_DISCOUNT("멤버십할인"),
    FINAL_AMOUNT("내실돈");

    private final String message;

    OutputMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}