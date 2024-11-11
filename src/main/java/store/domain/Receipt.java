package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<OrderItem> orderItems;
    private final List<FreeItem> freeItems;
    private final int totalAmount;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public Receipt(final List<OrderItem> orderItems, final List<FreeItem> freeItems,
                   final int totalAmount, final int promotionDiscount, final int membershipDiscount) {
        this.orderItems = new ArrayList<>(orderItems);
        this.freeItems = new ArrayList<>(freeItems);
        this.totalAmount = totalAmount;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public List<FreeItem> getFreeItems() {
        return new ArrayList<>(freeItems);
    }

    public int getTotalQuantity() {
        return orderItems.stream()
                .mapToInt(OrderItem::quantity)
                .sum();
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return totalAmount - promotionDiscount - membershipDiscount;
    }
}
