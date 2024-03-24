package th.co.prior.lab1.adventureshops.exception;

public class MarketItemNotFoundException extends RuntimeException {
    public MarketItemNotFoundException(String message) {
        super(message);
    }
}
