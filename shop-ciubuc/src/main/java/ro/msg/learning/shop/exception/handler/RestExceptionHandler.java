package ro.msg.learning.shop.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.msg.learning.shop.exception.AddressNotFoundException;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.exception.CouldNotFindProductException;
import ro.msg.learning.shop.exception.NoItemsFoundException;
import ro.msg.learning.shop.exception.OrderIsIncompleteException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CouldNotFindLocationException.class})
    protected ResponseEntity<Object> handleConflict(CouldNotFindLocationException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {NoItemsFoundException.class})
    protected ResponseEntity<Object> handleConflict(NoItemsFoundException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {CouldNotFindProductException.class})
    protected ResponseEntity<Object> handleConflict(CouldNotFindProductException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {AddressNotFoundException.class})
    protected ResponseEntity<Object> handleConflict(AddressNotFoundException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {OrderIsIncompleteException.class})
    protected ResponseEntity<Object> handleConflict(OrderIsIncompleteException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}