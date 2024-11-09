package store.view;

public class OutputView {

    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printInventoryListMessage() {
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printThankYouMessage() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }
}
