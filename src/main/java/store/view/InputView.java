package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;

import java.util.ArrayList;
import java.util.List;
import store.model.OrderItem;
import store.service.Inventory;

public class InputView {
    public String readItemInput() {
        return readLine();
    }

    public boolean readYesOrNo() {
        final String input = readLine().trim().toUpperCase();
        while (!input.equals("Y") && !input.equals("N")) {
            System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            final String newInput = readLine().trim().toUpperCase();
            if (newInput.equals("Y") || newInput.equals("N")) {
                return "Y".equals(newInput);
            }
        }
        return "Y".equals(input);
    }

    public List<OrderItem> parseOrderItems(final String input, final Inventory inventory) {
        final List<OrderItem> items = new ArrayList<>();
        // 입력에서 중간의 ],[ 만 제거
        final String[] tokens = input.split("],\\[");

        for (final String token : tokens) {
            // [ ] 모두 제거 및 -를 기준으로 물품명과 수량을 구분
            final String cleanedToken = token.replaceAll("[\\[\\]]", "");
            final String[] parts = cleanedToken.split("-");

            // 물품명-수량 두 부분이 아니면 에러
            if (parts.length != 2) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
            final String productName = parts[0].trim(); // 물품
            final int quantity; // 그 물품의 수량
            try {
                quantity = Integer.parseInt(parts[1].trim());
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("[ERROR] 수량은 양의 정수여야 합니다. 다시 입력해 주세요.");
            }


        }

    }


}
