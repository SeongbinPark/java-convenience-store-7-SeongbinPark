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
            continueShopping = checkContinueShopping();
        }
    }

    private boolean checkContinueShopping() {
        boolean continueShopping;
        while (true) {
            try {
                continueShopping = inputView.readContinueShopping();
                break;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
        return continueShopping;
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

        boolean useMembership;
        useMembership = isUseMembership();
        final Receipt receipt = storeService.generateReceipt(useMembership);
        outputView.printReceipt(receipt);
    }

    private boolean isUseMembership() {
        boolean useMembership;
        while (true) {
            try {
                useMembership = inputView.readMembershipChoice();
                break;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
        return useMembership;
    }
}
