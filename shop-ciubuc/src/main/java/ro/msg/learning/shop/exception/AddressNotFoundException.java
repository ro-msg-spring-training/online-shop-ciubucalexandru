package ro.msg.learning.shop.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException() {
        super("The wanted address was not found!");
    }
}
