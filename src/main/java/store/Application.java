package store;

import store.domain.Receipt;
import store.service.PromotionService;
import store.service.StoreService;
import store.util.FileLoader;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(final String[] args) {
        try {
            final StoreService storeService = new StoreService(
                    FileLoader.loadProducts(),
                    new PromotionService(FileLoader.loadPromotions())
            );
            final InputView inputView = new InputView();
            final OutputView outputView = new OutputView();

            runStore(storeService, inputView, outputView);
        } catch (final IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void runStore(final StoreService storeService, final InputView inputView,
                                 final OutputView outputView) {
        boolean continueShopping = true;
        while (continueShopping) {
            outputView.printWelcome();
            outputView.printProducts(storeService.getProducts());

            processOrder(storeService, inputView, outputView);
            continueShopping = inputView.readContinueShopping();
        }
    }

    private static void processOrder(final StoreService storeService, final InputView inputView,
                                     final OutputView outputView) {
        while (true) {
            try {
                final String orderInput = inputView.readOrder();
                storeService.processOrder(orderInput, inputView);

                final boolean useMembership = inputView.readMembershipChoice();
                final Receipt receipt = storeService.generateReceipt(useMembership);
                outputView.printReceipt(receipt);
                break;
            } catch (final IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }
}
