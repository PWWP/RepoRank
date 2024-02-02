package pk.reporank.backend.dto;

import lombok.Data;



@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;

    public RegisterDTO() {
    }
}
