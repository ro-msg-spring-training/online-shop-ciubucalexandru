package ro.msg.learning.shop.exception;

public class CouldNotFindLocationException extends RuntimeException {

    public CouldNotFindLocationException(String errorMessage) {
        super(errorMessage);
    }
}
