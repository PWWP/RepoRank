package pk.reporank.backend.dto.response;

import lombok.Data;


@Data
public class SuccessMessage {
    private int statusCode;
    private String message;
    private boolean error;

    public SuccessMessage(String message) {
        this.message = message;
        this.error = false;
        this.statusCode = 200;
    }
}
