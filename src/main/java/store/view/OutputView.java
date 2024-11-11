package store.view;

import java.util.List;
import store.constant.OutputMessages;
import store.domain.FreeItem;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Receipt;

public class OutputView {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final int NAME_WIDTH = 12;

    public void printWelcome() {
        System.out.println(OutputMessages.WELCOME.getMessage());
        System.out.println(OutputMessages.CURRENT_PRODUCTS.getMessage());
        System.out.println();
    }

    public void printWelcomeWithBlank() {
        System.out.println();
        printWelcome();
    }

    public void printProducts(final List<Product> products) {
        for (final Product product : products) {
            String promotionType = getPromotionTypeString(product);

            printAvailableProduct(product, promotionType);

            printUnavailableProduct(product, promotionType);
        }
        System.out.println();
    }

    private static String getPromotionTypeString(Product product) {
        String promotionType = "";
        if (product.hasPromotion()) {
            promotionType = " " + product.getPromotionType();
        }
        return promotionType;
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

    private static void printUnavailableProduct(Product product, String promotionType) {
        if (product.getTotalStock() <= 0) {
            System.out.printf(OutputMessages.PRODUCT_UNAVAILABLE.getMessage(),
                    product.getName(),
                    product.getPrice(),
                    promotionType);
        }
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

    private static void appendProductHeader(StringBuilder sb) {
        sb.append(LINE_SEPARATOR).append(OutputMessages.RECEIPT_HEADER.getMessage()).append(LINE_SEPARATOR);
        sb.append(String.format("%-" + NAME_WIDTH + "s\t\t수량\t\t금액%n", "상품명"));
    }

    private static void appendOrderItems(Receipt receipt, StringBuilder sb) {
        for (final OrderItem item : receipt.getOrderItems()) {
            sb.append(String.format("%-" + NAME_WIDTH + "s\t\t%d\t\t%,d%n",
                    item.product().getName(),
                    item.quantity(),
                    item.calculateTotalPrice()));
        }
    }

    private static void appendFreeItems(Receipt receipt, StringBuilder sb) {
        List<FreeItem> freeItems = receipt.getFreeItems();
//        if (freeItems.isEmpty()) {
//            return;
//        }
        sb.append(OutputMessages.RECEIPT_SECTION_FREE_ITEMS.getMessage()).append(LINE_SEPARATOR);
        for (final FreeItem item : freeItems) {
            sb.append(String.format("%-" + NAME_WIDTH + "s\t\t%d%n",
                    item.product().getName(),
                    item.quantity()));
        }
    }

    private static void appendTotalSectionHeader(StringBuilder sb) {
        sb.append(OutputMessages.RECEIPT_SECTION_TOTAL.getMessage()).append(LINE_SEPARATOR);
    }

    private static void appendTotal(Receipt receipt, StringBuilder sb) {
        sb.append(String.format("%-" + NAME_WIDTH + "s\t\t%d\t\t%,d%n",
                OutputMessages.TOTAL_PURCHASE.getMessage(),
                receipt.getTotalQuantity(),
                receipt.getTotalAmount()));
        sb.append(String.format("%-" + NAME_WIDTH + "s\t\t\t\t-%,d%n",
                OutputMessages.PROMOTION_DISCOUNT.getMessage(),
                receipt.getPromotionDiscount()));
        sb.append(String.format("%-" + NAME_WIDTH + "s\t\t\t\t-%,d%n",
                OutputMessages.MEMBERSHIP_DISCOUNT.getMessage(),
                receipt.getMembershipDiscount()));
        sb.append(String.format("%-" + NAME_WIDTH + "s\t\t\t\t %,d%n",
                OutputMessages.FINAL_AMOUNT.getMessage(),
                receipt.getFinalAmount()));
    }

    public void printError(final String message) {
        System.out.println(message);
    }
}
