package store.unit_test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.FreeItem;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Receipt;

public class ReceiptTest {
    @Test
    @DisplayName("영수증 금액이 정상적으로 계산된다")
    void calculateReceiptAmount() {
        // given
        Product product = new Product("콜라", 1000, 10, 0, "탄산2+1");
        List<OrderItem> orderItems = List.of(new OrderItem(product, 3));
        List<FreeItem> freeItems = List.of(new FreeItem(product, 1));

        // when
        Receipt receipt = new Receipt(orderItems, freeItems, 3000, 1000, 600);

        // then
        assertThat(receipt.getTotalAmount()).isEqualTo(3000);
        assertThat(receipt.getPromotionDiscount()).isEqualTo(1000);
        assertThat(receipt.getMembershipDiscount()).isEqualTo(600);
        assertThat(receipt.getFinalAmount()).isEqualTo(1400);
    }
}
