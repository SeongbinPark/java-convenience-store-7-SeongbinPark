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
}
