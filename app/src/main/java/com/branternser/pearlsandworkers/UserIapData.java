package com.branternser.pearlsandworkers0906;

public class UserIapData {
    private volatile int remainingOneTurn = 0;
    private volatile int remainingFiveTurn = 0;
    private volatile int remainingTenTurn = 0;
    private volatile int remainingFifteenTurn = 0;
    private volatile int remainingTwentyTurn = 0;
    private volatile int consumedOneTurn = 0;
    private volatile int consumedFiveTurn = 0;
    private volatile int consumedTenTurn = 0;
    private volatile int consumedFifteenTurn = 0;
    private volatile int consumedTwentyTurn = 0;

    private final String amazonUserId;
    private final String amazonMarketplace;

    public String getAmazonUserId() {
        return amazonUserId;
    }

    public String getAmazonMarketplace() {
        return amazonMarketplace;
    }

    public void setRemainingOneTurn(final int remaining) {
        this.remainingOneTurn = remaining;
    }

    public void setRemainingFiveTurn(final int remaining) {
        this.remainingFiveTurn = remaining;
    }

    public void setRemainingTenTurn(final int remaining) {
        this.remainingTenTurn = remaining;
    }

    public void setRemainingFifteenTurn(final int remaining) {
        this.remainingFifteenTurn = remaining;
    }

    public void setRemainingTwentyTurn(final int remaining) {
        this.remainingTwentyTurn = remaining;
    }

    public void setConsumedOneTurn(final int consumed) {
        this.consumedOneTurn = consumed;
    }
    public void setConsumedFiveTurn(final int consumed) {
        this.consumedFiveTurn = consumed;
    }

    public void setConsumedTenTurn(final int consumed) {
        this.consumedTenTurn = consumed;
    }

    public void setConsumedFifteenTurn(final int consumed) {
        this.consumedFifteenTurn = consumed;
    }
    public void setConsumedTwentyTurn(final int consumed) {
        this.consumedTwentyTurn = consumed;
    }

    public int getRemainingOneTurn() {
        return this.remainingOneTurn;
    }

    public int getRemainingFiveTurn() {
        return this.remainingFiveTurn;
    }

    public int getRemainingTenTurn() {
        return this.remainingTenTurn;
    }

    public int getRemainingFifteenTurn() {
        return this.remainingFifteenTurn;
    }

    public int getRemainingTwentyTurn() {
        return this.remainingTwentyTurn;
    }

    public int getConsumedOneTurn() {
        return this.consumedOneTurn;
    }

    public int getConsumedFiveTurn() {
        return this.consumedFiveTurn;
    }

    public int getConsumedTenTurn() {
        return this.consumedTenTurn;
    }

    public int getConsumedFifteenTurn() {
        return this.consumedFifteenTurn;
    }

    public int getConsumedTwentyTurn() {
        return this.consumedTwentyTurn;
    }

    public UserIapData(final String amazonUserId, final String amazonMarketplace) {
        this.amazonUserId = amazonUserId;
        this.amazonMarketplace = amazonMarketplace;
    }
}
