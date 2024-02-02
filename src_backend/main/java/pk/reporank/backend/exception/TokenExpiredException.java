package pk.reporank.backend.exception;

public class TokenExpiredException extends RuntimeException{

        public TokenExpiredException(String exception) {
            super(exception);
        }

}
