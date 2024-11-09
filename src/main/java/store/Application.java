package store;

import static store.util.DataLoader.loadProducts;
import static store.util.DataLoader.loadPromotions;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        List<Promotion> promotions = loadPromotions("promotions.md");

        List<Product> products = loadProducts("products.md", promotions);

    }
}
