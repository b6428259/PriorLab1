package th.co.prior.lab1.adventureshops.exception;

public class ItemAlreadySoldException extends RuntimeException {
    public ItemAlreadySoldException(String message) {
        super(message);
    }
}
