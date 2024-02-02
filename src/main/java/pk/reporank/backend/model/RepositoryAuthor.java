package pk.reporank.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Encja reprezentująca autora repozytorium.
 *
 * @author PWWP Paweł Wójcik
 */
@NoArgsConstructor
@Entity
@Getter
@Setter
@Data
@Table(name = "authors")
public class RepositoryAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Nazwa użytkownika autora.
     */
    @Column(nullable = false)
    private String username;

    /**
     * Imię autora.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Nazwisko autora.
     */
    @Column(nullable = false)
    private String surname;

    /**
     * URL do profilu zewnętrznego autora.
     */
    @Column()
    private String externalProfileUrl;

    @Column()
    private int contribution;
}
