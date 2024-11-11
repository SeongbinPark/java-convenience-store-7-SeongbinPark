package store.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import store.constant.ErrorMessages;
import store.domain.Product;
import store.domain.Promotion;

public class FileLoader {
    private static final String PRODUCTS_FILE = "products.md";
    private static final String PROMOTIONS_FILE = "promotions.md";
    private static final String COLUMN_DELIMITER = ",";
    private static final String NULL_STRING = "null";

    public static List<Product> loadProducts() {
        try (InputStream is = FileLoader.class.getClassLoader().getResourceAsStream(PRODUCTS_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            if (is == null) {
                throw new IllegalStateException(ErrorMessages.ERROR_LOADING_PRODUCTS.getMessage());
            }

            return reader.lines()
                    .skip(1) // Skip header
                    .map(FileLoader::createProductFromLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(ErrorMessages.ERROR_LOADING_PRODUCTS.getMessage(), e);
        }
    }

    public static List<Promotion> loadPromotions() {
        try (InputStream is = FileLoader.class.getClassLoader().getResourceAsStream(PROMOTIONS_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            if (is == null) {
                throw new IllegalStateException(ErrorMessages.ERROR_LOADING_PROMOTIONS.getMessage());
            }

            return reader.lines()
                    .skip(1) // Skip header
                    .map(FileLoader::parsePromotionFromLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(ErrorMessages.ERROR_LOADING_PROMOTIONS.getMessage(), e);
        }
    }

    private static Product createProductFromLine(String line) {
        final String[] columns = line.split(COLUMN_DELIMITER);
        validateProductColumns(columns);
        final String name = columns[0].trim();
        final int price = Integer.parseInt(columns[1].trim());
        final int quantity = Integer.parseInt(columns[2].trim());
        final String promotionType = initializePromotionType(columns[3].trim());
        final int promotionStock = calculatePromotionStock(promotionType, quantity);
        final int normalStock = calculateNormalStock(promotionType, quantity);
        return new Product(name, price, promotionStock, normalStock, promotionType);
    }

    private static Promotion parsePromotionFromLine(String line) {
        final String[] columns = line.split(COLUMN_DELIMITER);
        validatePromotionColumns(columns);
        final String name = columns[0].trim();
        final int buyQuantity = Integer.parseInt(columns[1].trim());
        final int getFreeQuantity = Integer.parseInt(columns[2].trim());
        final LocalDate startDate = LocalDate.parse(columns[3].trim());
        final LocalDate endDate = LocalDate.parse(columns[4].trim());
        return new Promotion(name, buyQuantity, getFreeQuantity, startDate, endDate);
    }

    private static List<Product> parseProducts(final List<String> lines) {
        final List<Product> products = new ArrayList<>();
        createProductFromLine(lines, products);
        return products;
    }

    private static void createProductFromLine(List<String> lines, List<Product> products) {
        for (final String line : lines) {
            final String[] columns = line.split(COLUMN_DELIMITER);
            validateProductColumns(columns);
            final String name = columns[0].trim();
            final int price = Integer.parseInt(columns[1].trim());
            final int quantity = Integer.parseInt(columns[2].trim());
            final String promotionType = initializePromotionType(columns[3].trim());
            final int promotionStock = calculatePromotionStock(promotionType, quantity);
            final int normalStock = calculateNormalStock(promotionType, quantity);
            products.add(new Product(name, price, promotionStock, normalStock, promotionType));
        }
    }

    private static String initializePromotionType(String rawPromotionType) {
        if (NULL_STRING.equals(rawPromotionType)) {
            return "";
        }
        return rawPromotionType;
    }

    private static int calculatePromotionStock(String promotionType, int quantity) {
        if (promotionType.isEmpty()) {
            return 0;
        }
        return quantity;
    }

    private static int calculateNormalStock(String promotionType, int quantity) {
        if (promotionType.isEmpty()) {
            return quantity;
        }
        return 0;
    }

    private static List<Promotion> parsePromotions(final List<String> lines) {
        final List<Promotion> promotions = new ArrayList<>();
        parsePromotionLine(lines, promotions);
        return promotions;
    }

    private static void parsePromotionLine(List<String> lines, List<Promotion> promotions) {
        for (final String line : lines) {
            final String[] columns = line.split(COLUMN_DELIMITER);
            validatePromotionColumns(columns);
            final String name = columns[0].trim();
            final int buyQuantity = Integer.parseInt(columns[1].trim());
            final int getFreeQuantity = Integer.parseInt(columns[2].trim());
            final LocalDate startDate = LocalDate.parse(columns[3].trim());
            final LocalDate endDate = LocalDate.parse(columns[4].trim());
            promotions.add(new Promotion(name, buyQuantity, getFreeQuantity, startDate, endDate));
        }
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
