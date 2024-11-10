package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;

import java.util.ArrayList;
import java.util.List;
import store.model.OrderItem;
import store.model.Product;
import store.model.Promotion;
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

            // 인벤토리에 그 상품명이 있나 확인 (프로모션상품, 일반상품 모두)
            final List<Product> matchedProducts = inventory.getProductsByName(productName);
            if (matchedProducts.isEmpty()) {
                throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
            }

            // 프로모션 재고, 일반 재고가 둘 다 있는 상품의 경우 프로모션 재고가 리스트의 앞에 있음
            Product product = matchedProducts.getFirst();
            Promotion promotion = product.getPromotion(); // 프로모션이 없는 경우 null
            OrderItem orderItem = new OrderItem(product, quantity);

            // 프로모션이 있는 경우
            if (promotion != null && promotion.isActive()) {
                int n = promotion.getBuyQuantity(); // n+1에서 n
                int one = promotion.getGetQuantity(); // 항상 1

                // 구매 수량이 프로모션을 적용할 정도로 충분한 경우
                if (n > 0 && quantity >= n) {
                    if (quantity == n) {
                        // 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N) 믄그 츠기
                    }

                    int freeItemCount = quantity / (n + 1); // 증정해야될 물품의 개수
                    int availableFreeItems = product.getStock();

                    if (availableFreeItems > 0) {
                        if (availableFreeItems >= freeItemCount) {
                            orderItem.addFreeQuantity(availableFreeItems);
                            System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n",
                                    productName, availableFreeItems);
                        }
                    }


                }
            }

            // 프로모션이 없는 경우

        }

    }


}
