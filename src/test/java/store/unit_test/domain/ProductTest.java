package store.unit_test.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("재고가 부족한 경우 예외가 발생한다")
    void validateInsufficientStock() {
        // given
        Product product = new Product("콜라", 1000, 5, 5, "탄산2+1");

        // when & then
        assertThatThrownBy(() -> product.processOrder(11, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }
}
