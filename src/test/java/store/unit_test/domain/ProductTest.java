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

    @Test
    @DisplayName("상품 생성 시 프로모션 재고와 일반 재고가 정상적으로 설정된다")
    void createProductWithStocks() {
        // given & when
        Product product = new Product("콜라", 1000, 5, 7, "탄산2+1");

        // then
        assertThat(product.getPromotionStock()).isEqualTo(5);
        assertThat(product.getNormalStock()).isEqualTo(7);
        assertThat(product.getTotalStock()).isEqualTo(12);
    }
}
