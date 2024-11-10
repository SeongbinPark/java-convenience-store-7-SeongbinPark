// src/main/java/store/util/FileLoader.java
package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

}