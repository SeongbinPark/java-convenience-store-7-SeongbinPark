package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<OrderItem> items;
    private final List<FreeItem> freeItems;

    public Cart() {
        this.items = new ArrayList<>();
        this.freeItems = new ArrayList<>();
    }

    public void addOrder(final Product product, final int quantity, final int promotionSets) {
        items.add(new OrderItem(product, quantity));
        if (promotionSets > 0) {
            // promotionSets가 정확한 증정품 개수
            freeItems.add(new FreeItem(product, promotionSets));
        }
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(items);
    }

    public List<FreeItem> getFreeItems() {
        return new ArrayList<>(freeItems);
    }

    public void clear() {
        items.clear();
        freeItems.clear();
    }
}
