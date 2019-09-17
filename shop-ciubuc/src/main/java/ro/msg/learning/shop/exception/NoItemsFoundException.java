package ro.msg.learning.shop.exception;

public class NoItemsFoundException extends RuntimeException {
    public NoItemsFoundException() {
        super("Could not find any items!");
    }
}
