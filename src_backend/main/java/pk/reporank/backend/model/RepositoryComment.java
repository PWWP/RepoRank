package pk.reporank.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import pk.reporank.backend.service.AppUserService;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Data
@Table(name = "comments")
public class RepositoryComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;
    private UUID repoId;
    private String content;
    public RepositoryComment(UUID userId, UUID repoId, String content) {
        this.userId = userId;
        this.repoId = repoId;
        this.content = content;
    }


}
