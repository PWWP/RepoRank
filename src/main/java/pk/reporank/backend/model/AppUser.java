package pk.reporank.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Entity
@AllArgsConstructor
@Data
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean activated;

    @Column(nullable = true)
    private String activationToken;
    @Column
    private String resetToken;

    @Column
    private Date resetTokenExpiryDate;

    @Column
    private String avatarUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id"))
    private Set<UserRole> roles = new HashSet<>();


    public AppUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.activated = false;
        this.activationToken = UUID.randomUUID().toString();
    }

    public AppUser() {

    }
}
