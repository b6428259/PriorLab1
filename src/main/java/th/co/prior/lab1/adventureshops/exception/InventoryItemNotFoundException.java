package th.co.prior.lab1.adventureshops.exception;


public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(String message) {
        super(message);
    }
}
