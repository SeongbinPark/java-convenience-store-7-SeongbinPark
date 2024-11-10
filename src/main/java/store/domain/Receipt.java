package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<OrderItem> orderItems;
    private final List<FreeItem> freeItems;
    private final int totalAmount;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public Receipt(List<OrderItem> orderItems, List<FreeItem> freeItems,
                   int totalAmount, int promotionDiscount, int membershipDiscount) {
        this.orderItems = new ArrayList<>(orderItems);
        this.freeItems = new ArrayList<>(freeItems);
        this.totalAmount = totalAmount;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }
}
