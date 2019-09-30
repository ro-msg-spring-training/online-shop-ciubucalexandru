package ro.msg.learning.shop.exception;

public class NoDistancesFoundException extends RuntimeException {
    public NoDistancesFoundException() {
        super("No distances to compare!");
    }
}
