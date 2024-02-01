package pk.reporank.backend.exception;

public class SportNotFoundException extends RuntimeException{

    public SportNotFoundException(Long exception) {
        super(String.valueOf(exception));
    }

}
