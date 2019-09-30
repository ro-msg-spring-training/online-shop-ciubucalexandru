package ro.msg.learning.shop.exception;

public class CouldNotConvertToCsvException extends RuntimeException {
    public CouldNotConvertToCsvException() {
        super("The requested items could not be written in the CSV format!");
    }
}
