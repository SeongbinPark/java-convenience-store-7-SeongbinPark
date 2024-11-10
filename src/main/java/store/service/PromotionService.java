package store.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.Promotion;

public class PromotionService {

    private final Map<String, Promotion> promotions;

    public PromotionService(List<Promotion> promotions) {
        this.promotions = promotions.stream()
                .collect(Collectors.toMap(Promotion::getName, p -> p));
    }

    public Promotion getPromotion(String promotionType) {
        return promotions.get(promotionType);
    }
}
