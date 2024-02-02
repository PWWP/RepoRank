package pk.reporank.backend.dto.response;

import lombok.Data;


@Data
public class ErrorMessage {
    private int statusCode;
    private String message;
    private String error;


    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
        this.error = message;
        this.statusCode = 400;
    }

    public ErrorMessage(int statusCode, String message, String error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }
}
