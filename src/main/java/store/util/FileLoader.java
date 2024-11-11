package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.constant.ErrorMessages;
import store.domain.Product;
import store.domain.Promotion;

public class FileLoader {
    private static final Path PRODUCTS_PATH = Paths.get("src/main/resources/products.md");
    private static final Path PROMOTIONS_PATH = Paths.get("src/main/resources/promotions.md");
    private static final String COLUMN_DELIMITER = ",";
    private static final String NULL_STRING = "null";

    public static List<Product> loadProducts() {
        try {
            final List<String> lines = Files.readAllLines(PRODUCTS_PATH);
            return parseProducts(lines.subList(1, lines.size())); // Skip header
        } catch (final IOException e) {
            throw new IllegalStateException(ErrorMessages.ERROR_LOADING_PRODUCTS.getMessage(), e);
        }
    }

    public static List<Promotion> loadPromotions() {
        try {
            final List<String> lines = Files.readAllLines(PROMOTIONS_PATH);
            return parsePromotions(lines.subList(1, lines.size())); // Skip header
        } catch (final IOException e) {
            throw new IllegalStateException(ErrorMessages.ERROR_LOADING_PROMOTIONS.getMessage(), e);
        }
    }

    private static List<Product> parseProducts(final List<String> lines) {
        final List<Product> products = new ArrayList<>();
        for (final String line : lines) {
            final String[] columns = line.split(COLUMN_DELIMITER);
            validateProductColumns(columns);

            final String name = columns[0];
            final int price = Integer.parseInt(columns[1]);
            final int quantity = Integer.parseInt(columns[2]);
            final String promotionType = NULL_STRING.equals(columns[3]) ? "" : columns[3];

            // promotionType이 있는 경우 프로모션 재고로, 없는 경우 일반 재고로 설정
            final int promotionStock = !promotionType.isEmpty() ? quantity : 0;
            final int normalStock = promotionType.isEmpty() ? quantity : 0;

            products.add(new Product(name, price, promotionStock, normalStock, promotionType));
        }
        return products;
    }

    private static List<Promotion> parsePromotions(final List<String> lines) {
        final List<Promotion> promotions = new ArrayList<>();
        for (final String line : lines) {
            final String[] columns = line.split(COLUMN_DELIMITER);
            validatePromotionColumns(columns);

            final String name = columns[0];
            final int buyQuantity = Integer.parseInt(columns[1]);
            final int getFreeQuantity = Integer.parseInt(columns[2]);
            final LocalDate startDate = LocalDate.parse(columns[3]);
            final LocalDate endDate = LocalDate.parse(columns[4]);

            promotions.add(new Promotion(name, buyQuantity, getFreeQuantity, startDate, endDate));
        }
        return promotions;
    }

    private static void validateProductColumns(final String[] columns) {
        if (columns.length != 4) {
            throw new IllegalStateException(ErrorMessages.INVALID_PRODUCT_FORMAT.getMessage());
        }
        validateNumericValue(columns[1], "상품 가격");
        validateNumericValue(columns[2], "상품 수량");
    }

    private static void validatePromotionColumns(final String[] columns) {
        if (columns.length != 5) {
            throw new IllegalStateException(ErrorMessages.INVALID_PROMOTION_FORMAT.getMessage());
        }
        validateNumericValue(columns[1], "구매 수량");
        validateNumericValue(columns[2], "증정 수량");
        validateDateFormat(columns[3], "시작 날짜");
        validateDateFormat(columns[4], "종료 날짜");
    }

    private static void validateNumericValue(final String value, final String fieldName) {
        try {
            final int numericValue = Integer.parseInt(value);
            if (numericValue < 0) {
                throw new IllegalStateException(
                        String.format(ErrorMessages.INVALID_QUANTITY_FORMAT.getMessage(), fieldName));
            }
        } catch (final NumberFormatException e) {
            throw new IllegalStateException(
                    String.format(ErrorMessages.INVALID_QUANTITY_FORMAT.getMessage(), fieldName));
        }
    }

    private static void validateDateFormat(final String value, final String fieldName) {
        try {
            LocalDate.parse(value);
        } catch (final Exception e) {
            throw new IllegalStateException(
                    String.format(ErrorMessages.INVALID_DATE_FORMAT.getMessage(), fieldName));
        }
    }
}
