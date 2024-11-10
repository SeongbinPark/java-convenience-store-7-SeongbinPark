// src/main/java/store/util/FileLoader.java
package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public class FileLoader {
    private static final Path PRODUCTS_PATH = Paths.get("src/main/resources/products.md");
    private static final Path PROMOTIONS_PATH = Paths.get("src/main/resources/promotions.md");
    private static final String COLUMN_DELIMITER = ",";
    private static final String NULL_STRING = "null";

    public static List<Product> loadProducts() {
        try {
            List<String> lines = Files.readAllLines(PRODUCTS_PATH);
            return parseProducts(lines.subList(1, lines.size())); // Skip header
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 상품 정보를 불러올 수 없습니다.", e);
        }
    }

    public static List<Promotion> loadPromotions() {
        try {
            List<String> lines = Files.readAllLines(PROMOTIONS_PATH);
            return parsePromotions(lines.subList(1, lines.size())); // Skip header
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 프로모션 정보를 불러올 수 없습니다.", e);
        }
    }

    private static List<Product> parseProducts(List<String> lines) {
        List<Product> products = new ArrayList<>();
        for (String line : lines) {
            String[] columns = line.split(COLUMN_DELIMITER);
            validateProductColumns(columns);

            String name = columns[0];
            int price = Integer.parseInt(columns[1]);
            int quantity = Integer.parseInt(columns[2]);
            String promotionType = NULL_STRING.equals(columns[3]) ? "" : columns[3];

            // promotionType이 있는 경우 프로모션 재고로, 없는 경우 일반 재고로 설정
            int promotionStock = !promotionType.isEmpty() ? quantity : 0;
            int normalStock = promotionType.isEmpty() ? quantity : 0;

            products.add(new Product(name, price, promotionStock, normalStock, promotionType));
        }
        return products;
    }

    private static List<Promotion> parsePromotions(List<String> lines) {
        List<Promotion> promotions = new ArrayList<>();
        for (String line : lines) {
            String[] columns = line.split(COLUMN_DELIMITER);
            validatePromotionColumns(columns);

            String name = columns[0];
            int buyQuantity = Integer.parseInt(columns[1]);
            int getFreeQuantity = Integer.parseInt(columns[2]);
            LocalDate startDate = LocalDate.parse(columns[3]);
            LocalDate endDate = LocalDate.parse(columns[4]);

            promotions.add(new Promotion(name, buyQuantity, getFreeQuantity, startDate, endDate));
        }
        return promotions;
    }

    private static void validateProductColumns(String[] columns) {
        if (columns.length != 4) {
            throw new IllegalStateException("[ERROR] 상품 정보 형식이 올바르지 않습니다.");
        }
        validateNumericValue(columns[1], "상품 가격");
        validateNumericValue(columns[2], "상품 수량");
    }

    private static void validatePromotionColumns(String[] columns) {
        if (columns.length != 5) {
            throw new IllegalStateException("[ERROR] 프로모션 정보 형식이 올바르지 않습니다.");
        }
        validateNumericValue(columns[1], "구매 수량");
        validateNumericValue(columns[2], "증정 수량");
        validateDateFormat(columns[3], "시작 날짜");
        validateDateFormat(columns[4], "종료 날짜");
    }

    private static void validateNumericValue(String value, String fieldName) {
        try {
            int numericValue = Integer.parseInt(value);
            if (numericValue < 0) {
                throw new IllegalStateException(
                        String.format("[ERROR] %s는 0보다 작을 수 없습니다.", fieldName));
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    String.format("[ERROR] %s 형식이 올바르지 않습니다.", fieldName));
        }
    }

    private static void validateDateFormat(String value, String fieldName) {
        try {
            LocalDate.parse(value);
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("[ERROR] %s 형식이 올바르지 않습니다. (YYYY-MM-DD)", fieldName));
        }
    }
}