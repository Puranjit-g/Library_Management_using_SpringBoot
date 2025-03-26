package org.jbd.JBD_MINOR1.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(value = TxnException.class)
    //ResponseEntity has a Http class that will return a msg to postman or to the user 
    public ResponseEntity<Object> handle(TxnException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserException.class)
    //ResponseEntity has a Http class that will return a msg to postman or to the user
    public ResponseEntity<Object> handle(UserException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    //ResponseEntity has a Http class that will return a msg to postman or to the user
    public ResponseEntity<Object> handle(MethodArgumentNotValidException e){
        return new ResponseEntity<>(e.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
