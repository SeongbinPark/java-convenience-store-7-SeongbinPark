package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.constant.ErrorMessages;
import store.constant.InputMessages;
import store.domain.Product;

public class InputView {
    public String readOrder() {
        System.out.println(InputMessages.ORDER_PROMPT_MESSAGE.getMessage());
        return Console.readLine();
    }

    public boolean readPromotionAddition(final Product product) {
        System.out.println();
        System.out.printf(InputMessages.PROMOTION_ADDITION_MESSAGE_TEMPLATE.getMessage(),
                product.getName());
        return readYesNo();
    }

    public boolean readNormalPriceConfirmation(final Product product, final int quantity) {
        System.out.println();
        System.out.printf(InputMessages.NORMAL_PRICE_CONFIRMATION_MESSAGE_TEMPLATE.getMessage(),
                product.getName(), quantity);
        System.out.println();
        return readYesNo();
    }

    public boolean readMembershipChoice() {
        System.out.println();
        System.out.println(InputMessages.MEMBERSHIP_CHOICE_MESSAGE.getMessage());
        return readYesNo();
    }

    public boolean readContinueShopping() {
        System.out.println();
        System.out.println(InputMessages.CONTINUE_SHOPPING_MESSAGE.getMessage());
        return readYesNo();
    }

    private boolean readYesNo() {
        final String input = Console.readLine().toUpperCase().trim();
        validateYesNo(input);
        return "Y".equals(input);
    }

    private void validateYesNo(final String input) {
        if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_YES_NO_INPUT.getMessage());
        }
    }
}
