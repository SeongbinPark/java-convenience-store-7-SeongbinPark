package store.unit_test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.service.PromotionService;
import store.util.FileLoader;

public class PromotionServiceTest {
    private PromotionService promotionService;
    private List<Promotion> promotions;
    private Product product;

    @BeforeEach
    void setUp() {
        promotions = FileLoader.loadPromotions();
        promotionService = new PromotionService(promotions);
        product = new Product("콜라", 1000, 10, 0, "탄산2+1");
    }

    @Test
    @DisplayName("유효한 프로모션을 가진 상품은 프로모션 적용이 가능하다")
    void canApplyValidPromotion() {
        assertThat(promotionService.canApplyPromotion(product)).isTrue();
    }

}