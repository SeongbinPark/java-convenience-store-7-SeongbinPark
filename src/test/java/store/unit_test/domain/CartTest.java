package store.unit_test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Cart;
import store.domain.FreeItem;
import store.domain.OrderItem;
import store.domain.Product;

public class CartTest {
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        product = new Product("콜라", 1000, 10, 0, "탄산2+1");
    }

    @Test
    @DisplayName("장바구니에 상품 추가시 주문 항목이 정상적으로 저장된다")
    void addOrderToCart() {
        // when
        cart.addOrder(product, 3, 1);

        // then
        List<OrderItem> orderItems = cart.getOrderItems();
        List<FreeItem> freeItems = cart.getFreeItems();

        assertThat(orderItems).hasSize(1);
        assertThat(freeItems).hasSize(1);
        assertThat(orderItems.get(0).quantity()).isEqualTo(3);
        assertThat(freeItems.get(0).quantity()).isEqualTo(1);
    }
}
