package pk.reporank.backend.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class SuccessMessageWithPagination<T> {
    private int statusCode;
    private String message;
    private boolean error;
    private List<T> data;
    private int limit;
    private int pageNumber;
}
