package pk.reporank.backend.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UUID id;
    private String username;
    private String email;

    private Date expiration;
    private List<String> roles;

    public JwtResponse(String accessToken, UUID id, String username, String email, List<String> roles, Date expiration) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.expiration = expiration;
    }

}
