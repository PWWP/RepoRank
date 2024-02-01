package pk.reporank.backend.exception;

import java.util.UUID;

public class TeamNotFoundException extends RuntimeException{

    public TeamNotFoundException(UUID exception) {
        super(String.valueOf(exception));
    }

}
