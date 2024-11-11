package store.integration_test;

import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.test.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Receipt;
import store.service.PromotionService;
import store.service.StoreService;
import store.util.FileLoader;
import store.view.InputView;

public class StoreIntegrationTest {
    @Test
    @DisplayName("2+1 상품 3개 구매시 정상적으로 처리된다")
    void buyThreeItemsWithTwoPlusOnePromotion() {
        // given
        String input = "[콜라-3]";
        StoreService storeService = new StoreService(
                FileLoader.loadProducts(),
                new PromotionService(FileLoader.loadPromotions())
        );

        // when
        Assertions.assertSimpleTest(() -> {
            storeService.processOrder(input, new InputView());
            Receipt receipt = storeService.generateReceipt(false);

            // then
            assertThat(receipt.getTotalAmount()).isEqualTo(3000);
            assertThat(receipt.getPromotionDiscount()).isEqualTo(1000);
            assertThat(receipt.getFinalAmount()).isEqualTo(2000);
            assertThat(receipt.getFreeItems()).hasSize(1);
            assertThat(receipt.getFreeItems().get(0).quantity()).isEqualTo(1);
        });
    }
}
