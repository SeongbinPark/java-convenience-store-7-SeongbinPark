package store.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import store.model.Product;
import store.model.Promotion;

public class DataLoader {
    public static List<Promotion> loadPromotions(String filePath) {
        try (InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            return reader.lines()
                    .skip(1) // 헤더 스킵
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 5)
                    .map(parts -> new Promotion(
                            parts[0].trim(),
                            Integer.parseInt(parts[1].trim()),
                            Integer.parseInt(parts[2].trim()),
                            LocalDate.parse(parts[3].trim()),
                            LocalDate.parse(parts[4].trim())
                    ))
                    .collect(Collectors.toList());
        } catch (IOException | NumberFormatException | NullPointerException e) {
            throw new RuntimeException("[ERROR] 프로모션 데이터를 로드하는 중 문제가 발생했습니다.", e);
        }
    }

    public static List<Product> loadProducts(String filePath, List<Promotion> promotions) {
        try (InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            return reader.lines()
                    .skip(1) // 헤더 스킵
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 4)
                    .map(parts -> {
                        String name = parts[0].trim();
                        int price = Integer.parseInt(parts[1].trim());
                        int quantity = Integer.parseInt(parts[2].trim());
                        String promotionName = parts[3].trim();
                        Promotion promotion = null;
                        if (!promotionName.equalsIgnoreCase("null")) {
                            promotion = promotions.stream()
                                    .filter(promo -> promo.getName().equalsIgnoreCase(promotionName)
                                            && promo.isActive())
                                    .findFirst()
                                    .orElse(null);
                        }
                        return new Product(name, price, quantity, promotion);
                    })
                    .collect(Collectors.toList());
        } catch (IOException | NumberFormatException | NullPointerException e) {
            throw new RuntimeException("[ERROR] 상품 데이터를 로드하는 중 문제가 발생했습니다.", e);
        }
    }
}
