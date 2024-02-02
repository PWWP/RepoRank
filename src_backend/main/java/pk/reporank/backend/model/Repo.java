package pk.reporank.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * @author PWWP Paweł Wójcik
 */
@Entity
@Getter
@Setter
@Data
@Table(name = "repo")
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public Repo() {
    }

    private String name;
    private String comment;
    private String url;
    private String image;
    private long addTime;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private AppUser createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RepoReaction> reactions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RepositoryComment> comments;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RepositoryAuthor> authors;

    @Transient
    public long getLikes() {
        return reactions.stream().filter(repoReaction -> repoReaction.getReactionType() == ReactionType.LIKE).count();
    }


    @Transient
    public long getUnlikes() {
        return reactions.stream().filter(repoReaction -> repoReaction.getReactionType() == ReactionType.DISLIKE).count();
    }


    @Transient
    public String getCreateDate() {
        // Konwertuj addTime na LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(addTime), ZoneId.systemDefault());

        // Formatuj LocalDateTime jako ciąg znaków
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

}
