package pk.reporank.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Data
@Table(name = "reactions")
public class RepoReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID repoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;

    public RepoReaction(UUID userId, UUID repoId, ReactionType reactionType) {
        this.userId = userId;
        this.repoId = repoId;
        this.reactionType = reactionType;
    }
}
