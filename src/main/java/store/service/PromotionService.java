package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.Product;
import store.domain.Promotion;

public class PromotionService {

    private final Map<String, Promotion> promotions;

    public PromotionService(final List<Promotion> promotions) {
        this.promotions = promotions.stream()
                .collect(Collectors.toMap(Promotion::getName, p -> p));
    }

    public Promotion getPromotion(final String promotionType) {
        return promotions.get(promotionType);
    }

    public boolean canApplyPromotion(final Product product) {
        return product.hasPromotion() &&
                promotions.containsKey(product.getPromotionType()) &&
                promotions.get(product.getPromotionType())
                        .isValid(DateTimes.now().toLocalDate());
    }
}
