package ro.msg.learning.shop.exception;

public class CouldNotFindProductCategoryException extends RuntimeException {
    public CouldNotFindProductCategoryException() {
        super("Could not find the wanted product category!");
    }
}
