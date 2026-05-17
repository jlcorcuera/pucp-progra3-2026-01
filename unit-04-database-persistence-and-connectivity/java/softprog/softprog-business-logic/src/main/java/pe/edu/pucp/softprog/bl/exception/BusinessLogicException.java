package pe.edu.pucp.softprog.bl.exception;

public class BusinessLogicException extends Exception {

    public BusinessLogicException() {
        super();
    }

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(Exception ex) {
        super(ex);
    }
}
