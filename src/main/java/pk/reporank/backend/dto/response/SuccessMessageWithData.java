package pk.reporank.backend.dto.response;

import lombok.Data;


@Data
public class SuccessMessageWithData<T> {
    private int statusCode;
    private String message;
    private boolean error;
    private T data;


    public SuccessMessageWithData() {
    }

    public SuccessMessageWithData(T data) {
        this.error = false;
        this.data = data;
        this.statusCode = 200;
    }

    public SuccessMessageWithData(T data, int statusCode) {
        this.error = false;
        this.data = data;
        this.statusCode = statusCode;
    }
}
