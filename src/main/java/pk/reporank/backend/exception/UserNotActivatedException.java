package pk.reporank.backend.exception;

public class UserNotActivatedException extends RuntimeException{

    public UserNotActivatedException(String exception) {
        super(exception);
    }

}