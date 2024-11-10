// src/main/java/store/view/OutputView.java
package store.view;

import store.domain.Receipt;

public class OutputView {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public void printReceipt(Receipt receipt) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===========W 편의점=============").append(LINE_SEPARATOR);
        sb.append(String.format("%-12s %4s %10s%n", "상품명", "수량", "금액")); // 제목의 너비 조정

        System.out.print(sb);
    }

}