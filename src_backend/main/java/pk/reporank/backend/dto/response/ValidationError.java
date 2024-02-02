package pk.reporank.backend.dto.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class ValidationError {

    private int statusCode;
    private Map<String, String> message;
    private String error;

    public ValidationError() {
        this.message = new HashMap<>();
    }
}
