package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Product;

public class InputView {
    public String readOrder() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    public boolean readPromotionAddition(final Product product) {
        System.out.println();
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n",
                product.getName());
        return readYesNo();
    }

    public boolean readNormalPriceConfirmation(final Product product, final int quantity) {
        System.out.println();
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n",
                product.getName(), quantity);
        return readYesNo();
    }

    public boolean readMembershipChoice() {
        System.out.println();
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return readYesNo();
    }

    public boolean readContinueShopping() {
        System.out.println();
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return readYesNo();
    }

    private boolean readYesNo() {
        final String input = Console.readLine().toUpperCase().trim();
        validateYesNo(input);
        return "Y".equals(input);
    }

    private void validateYesNo(final String input) {
        if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
            throw new IllegalArgumentException("[ERROR] Y 또는 N만 입력 가능합니다. 다시 입력해 주세요.");
        }
    }
}
