package store.view;

import java.util.List;
import store.constant.OutputMessages;
import store.domain.FreeItem;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Receipt;

public class OutputView {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public void printWelcome() {
        System.out.println(OutputMessages.WELCOME.getMessage());
        System.out.println(OutputMessages.CURRENT_PRODUCTS.getMessage());
    }

    public void printProducts(final List<Product> products) {
        for (final Product product : products) {
            String promotionType = getPromotionTypeString(product);

            printAvailableProduct(product, promotionType);

            printUnavailableProduct(product, promotionType);
        }
        System.out.println();
    }

    private static void printUnavailableProduct(Product product, String promotionType) {
        if (product.getTotalStock() <= 0) {
            System.out.printf(OutputMessages.PRODUCT_UNAVAILABLE.getMessage(),
                    product.getName(),
                    product.getPrice(),
                    promotionType);
        }
    }

    private static void printAvailableProduct(Product product, String promotionType) {
        if (product.getTotalStock() > 0) {
            System.out.printf(OutputMessages.PRODUCT_AVAILABLE.getMessage(),
                    product.getName(),
                    product.getPrice(),
                    product.getTotalStock(),
                    promotionType);
        }
    }

    private static String getPromotionTypeString(Product product) {
        String promotionType = "";
        if (product.hasPromotion()) {
            promotionType = " " + product.getPromotionType();
        }
        return promotionType;
    }

    public void printReceipt(final Receipt receipt) {
        final StringBuilder sb = new StringBuilder();
        appendProductHeader(sb);
        appendOrderItems(receipt, sb);
        appendFreeItems(receipt, sb);
        appendTotalSectionHeader(sb);
        appendTotal(receipt, sb);
        System.out.print(sb);
    }

    private static void appendTotal(Receipt receipt, StringBuilder sb) {
        sb.append(String.format("%-12s %4d %,10d%n",
                OutputMessages.TOTAL_PURCHASE.getMessage(), receipt.getTotalQuantity(), receipt.getTotalAmount()));
        sb.append(String.format("%-12s %,14d%n",
                OutputMessages.PROMOTION_DISCOUNT.getMessage(), -receipt.getPromotionDiscount()));
        sb.append(String.format("%-12s %,14d%n",
                OutputMessages.MEMBERSHIP_DISCOUNT.getMessage(), -receipt.getMembershipDiscount()));
        sb.append(String.format("%-12s %,14d%n",
                OutputMessages.FINAL_AMOUNT.getMessage(), receipt.getFinalAmount()));
    }

    private static void appendTotalSectionHeader(StringBuilder sb) {
        sb.append(OutputMessages.RECEIPT_SECTION_TOTAL.getMessage()).append(LINE_SEPARATOR);
    }

    private static void appendFreeItems(Receipt receipt, StringBuilder sb) {
        sb.append(OutputMessages.RECEIPT_SECTION_FREE_ITEMS.getMessage()).append(LINE_SEPARATOR);
        for (final FreeItem item : receipt.getFreeItems()) {
            sb.append(String.format("%-12s %4d%n",
                    item.product().getName(),
                    item.quantity()));
        }
    }

    private static void appendOrderItems(Receipt receipt, StringBuilder sb) {
        for (final OrderItem item : receipt.getOrderItems()) {
            sb.append(String.format("%-12s %4d %,10d%n",
                    item.product().getName(),
                    item.quantity(),
                    item.calculateTotalPrice()));
        }
    }

    private static void appendProductHeader(StringBuilder sb) {
        sb.append(OutputMessages.RECEIPT_HEADER.getMessage()).append(LINE_SEPARATOR);
        sb.append(String.format(OutputMessages.RECEIPT_PRODUCT_HEADER.getMessage(),
                "상품명", "수량", "금액")).append(LINE_SEPARATOR); // 제목의 너비 조정
    }

    public void printError(final String message) {
        System.out.println(message);
    }
}
