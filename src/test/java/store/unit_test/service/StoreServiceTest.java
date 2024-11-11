package store.unit_test.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import camp.nextstep.edu.missionutils.test.Assertions;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.service.PromotionService;
import store.service.StoreService;
import store.util.FileLoader;
import store.view.InputView;

public class StoreServiceTest {
    private StoreService storeService;
    private InputView inputView;
    private List<Product> products;
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        products = FileLoader.loadProducts();
        promotionService = new PromotionService(FileLoader.loadPromotions());
        storeService = new StoreService(products, promotionService);
        inputView = new InputView();
    }

    @Test
    @DisplayName("올바른 주문 형식이 아닌 경우 예외가 발생한다")
    void validateInvalidOrderFormat() {
        // given
        String invalidOrder = "콜라-3,사이다-2";

        // when & then
        Assertions.assertSimpleTest(() -> {
            assertThatThrownBy(() -> storeService.processOrder(invalidOrder, inputView))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("올바르지 않은 형식으로 입력했습니다");
        });
    }

    @Test
    @DisplayName("존재하지 않는 상품 주문시 예외가 발생한다")
    void orderNonExistentProduct() {
        // given
        String invalidOrder = "[환타-1]";

        // when & then
        Assertions.assertSimpleTest(() -> {
            assertThatThrownBy(() -> storeService.processOrder(invalidOrder, inputView))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 상품입니다");
        });
    }
}
