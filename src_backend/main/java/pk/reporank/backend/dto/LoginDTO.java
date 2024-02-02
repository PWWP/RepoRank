package pk.reporank.backend.dto;

import lombok.Data;



@Data
public class LoginDTO {
        private String username;
        private String password;
        public LoginDTO() {}
}
