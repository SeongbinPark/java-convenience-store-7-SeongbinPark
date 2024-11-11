package store.domain;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int getFreeQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(final String name, final int buyQuantity, final int getFreeQuantity,
                     final LocalDate startDate, final LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getFreeQuantity = getFreeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getFreeQuantity() {
        return getFreeQuantity;
    }

    public boolean isValid(final LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
