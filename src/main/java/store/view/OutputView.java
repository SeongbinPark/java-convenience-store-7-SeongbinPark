// src/main/java/store/view/OutputView.java
package store.view;

import java.util.List;
import store.domain.Product;
import store.domain.Receipt;

public class OutputView {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public void printWelcome() {
        System.out.println("\n안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printProducts(List<Product> products) {
        for (Product product : products) {
            if (product.getTotalStock() > 0) {
                System.out.printf("- %s %,d원 %d개%s%n",
                        product.getName(),
                        product.getPrice(),
                        product.getTotalStock(),
                        product.hasPromotion() ? " " + product.getPromotionType() : "");
            } else {
                System.out.printf("- %s %,d원 재고 없음%s%n",
                        product.getName(),
                        product.getPrice(),
                        product.hasPromotion() ? " " + product.getPromotionType() : "");
            }
        }
        System.out.println();
    }
    
    public void printReceipt(Receipt receipt) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===========W 편의점=============").append(LINE_SEPARATOR);
        sb.append(String.format("%-12s %4s %10s%n", "상품명", "수량", "금액")); // 제목의 너비 조정

        System.out.print(sb);
    }

    public void printError(String message) {
        System.out.println(message);
    }
}