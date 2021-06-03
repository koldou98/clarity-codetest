package es.koldo.clarity.codetest.exceptions;

public class FileDoesNotExistException extends Exception {
    public FileDoesNotExistException(String message) {
        super(message);
    }
}
