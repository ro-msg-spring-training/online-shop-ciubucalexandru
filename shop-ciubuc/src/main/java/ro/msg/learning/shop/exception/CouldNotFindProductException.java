package ro.msg.learning.shop.exception;

public class CouldNotFindProductException extends RuntimeException {
    public CouldNotFindProductException() {
        super("Could not find the wanted product!");
    }
}
