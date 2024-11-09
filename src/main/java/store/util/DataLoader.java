package store.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import store.Promotion;

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
}
