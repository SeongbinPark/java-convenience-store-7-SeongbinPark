package store.unit_test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;

class ProductTest {
    @Test
    @DisplayName("상품 생성 시 프로모션 타입이 null이면 빈 문자열로 설정된다")
    void createProductWithNullPromotionType() {
        // given & when
        Product product = new Product("콜라", 1000, 10, 10, null);

        // then
        assertThat(product.getPromotionType()).isEmpty();
    }

}