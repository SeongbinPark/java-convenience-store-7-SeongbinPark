package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int getQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buyQuantity, int getQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetQuantity() {
        return getQuantity;
    }

    public boolean isActive() {
        LocalDate today = DateTimes.now().toLocalDate();
        return (today.isEqual(startDate) || today.isAfter(startDate)) &&
                (today.isEqual(endDate) || today.isBefore(endDate));
    }
}
