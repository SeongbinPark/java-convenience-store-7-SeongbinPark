package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;

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

}
