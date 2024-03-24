package th.co.prior.lab1.adventureshops.exception;


public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
