package store;

import static store.util.DataLoader.loadProducts;
import static store.util.DataLoader.loadPromotions;

import java.util.List;
import store.model.Product;
import store.model.Promotion;
import store.service.Inventory;

public class Application {
    public static void main(String[] args) {
        List<Promotion> promotions = loadPromotions("promotions.md");

        List<Product> products = loadProducts("products.md", promotions);

        Inventory inventory = new Inventory(products);

    }
}
