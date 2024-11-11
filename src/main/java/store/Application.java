// src/main/java/store/Application.java
package store;

import store.domain.Receipt;
import store.service.PromotionService;
import store.service.StoreService;
import store.util.FileLoader;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        try {
            StoreService storeService = new StoreService(
                    FileLoader.loadProducts(),
                    new PromotionService(FileLoader.loadPromotions())
            );
            InputView inputView = new InputView();
            OutputView outputView = new OutputView();

            runStore(storeService, inputView, outputView);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void runStore(StoreService storeService, InputView inputView, OutputView outputView) {
        boolean continueShopping = true;
        while (continueShopping) {
            outputView.printWelcome();
            outputView.printProducts(storeService.getProducts());

            processOrder(storeService, inputView, outputView);
            continueShopping = inputView.readContinueShopping();
        }
    }

    private static void processOrder(StoreService storeService, InputView inputView, OutputView outputView) {
        while (true) {
            try {
                String orderInput = inputView.readOrder();
                storeService.processOrder(orderInput, inputView);

                boolean useMembership = inputView.readMembershipChoice();
                Receipt receipt = storeService.generateReceipt(useMembership);
                outputView.printReceipt(receipt);
                break;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }
}
