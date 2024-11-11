package store;

import store.domain.Receipt;
import store.service.PromotionService;
import store.service.StoreService;
import store.util.FileLoader;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    private final StoreService storeService;
    private final InputView inputView;
    private final OutputView outputView;

    public Application() {
        this.storeService = new StoreService(
                FileLoader.loadProducts(),
                new PromotionService(FileLoader.loadPromotions())
        );
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    public static void main(String[] args) {
        try {
            Application app = new Application();
            app.runStore();
        } catch (final IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void runStore() {
        boolean continueShopping = true;
        while (continueShopping) {
            outputView.printWelcome();
            outputView.printProducts(storeService.getProducts());

            processOrder();
            continueShopping = inputView.readContinueShopping();
        }
    }

    private void processOrder() {
        while (true) {
            try {
                executeSingleOrder();
                break;
            } catch (final IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    private void executeSingleOrder() {
        final String orderInput = inputView.readOrder();
        storeService.processOrder(orderInput, inputView);

        final boolean useMembership = inputView.readMembershipChoice();
        final Receipt receipt = storeService.generateReceipt(useMembership);
        outputView.printReceipt(receipt);
    }
}
