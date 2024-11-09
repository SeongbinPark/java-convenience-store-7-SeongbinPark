package store;

import static store.util.DataLoader.loadProducts;
import static store.util.DataLoader.loadPromotions;

import java.util.List;
import store.model.Product;
import store.model.Promotion;
import store.service.Inventory;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        List<Promotion> promotions = loadPromotions("promotions.md");

        List<Product> products = loadProducts("products.md", promotions);

        Inventory inventory = new Inventory(products);

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        boolean continueShopping = true;

        while (continueShopping) {

            outputView.printWelcomeMessage();
            outputView.printInventoryListMessage();

            inventory.displayProducts();

            // 구매하려는  입력
            String input = inputView.readItemInput();

            // 마지막 부분
            outputView.printThankYouMessage();
            continueShopping = inputView.readYesOrNo();


        }


    }
}
