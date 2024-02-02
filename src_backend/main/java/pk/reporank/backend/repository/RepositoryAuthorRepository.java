package pk.reporank.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.reporank.backend.model.RepositoryAuthor;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepositoryAuthorRepository extends JpaRepository<RepositoryAuthor, UUID> {
    Optional<RepositoryAuthor> findByUsername(String username);

}
