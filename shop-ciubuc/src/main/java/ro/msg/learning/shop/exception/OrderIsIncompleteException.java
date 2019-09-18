package ro.msg.learning.shop.exception;

public class OrderIsIncompleteException extends RuntimeException {
    public OrderIsIncompleteException() {
        super("Could not find enough items to complete the order!");
    }
}
