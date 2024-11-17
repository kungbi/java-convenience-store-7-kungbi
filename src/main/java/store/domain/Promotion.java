package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
        validate(name, buy, get, startDate, endDate);
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isAvailable() {
        return startDate.isAfter(DateTimes.now()) && endDate.isBefore(DateTimes.now());
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    private void validate(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (buy <= 0) {
            throw new IllegalArgumentException("Buy cannot be negative");
        }
        if (get <= 0) {
            throw new IllegalArgumentException("Get cannot be negative");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }
}
