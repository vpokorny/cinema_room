package cinema;


public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String error) {
        super(error);
    }
}
