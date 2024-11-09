package store.service;

import java.util.ArrayList;
import java.util.List;
import store.model.Product;

public class Inventory {
    private final List<Product> products;

    public Inventory(final List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public List<Product> getProductsByName(final String name) {
        List<Product> matchedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                matchedProducts.add(product);
            }
        }
        return matchedProducts;
    }

    public void displayProducts() {
        for (Product product : products) {
            String stockInfo;
            String promotionInfo;

            if (product.getStock() > 0) {
                stockInfo = product.getStock() + "개";
            } else {
                stockInfo = "재고 없음";
            }

            if (product.hasPromotion()) {
                promotionInfo = product.getPromotion().getName();
            } else {
                promotionInfo = "";
            }
            System.out.printf("- %s %d원 %s %s%n",
                    product.getName(),
                    product.getPrice(),
                    stockInfo,
                    promotionInfo);
        }
        System.out.println();
    }
}
