package pk.reporank.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.reporank.backend.model.RepositoryComment;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepositoryCommentRepository extends JpaRepository<RepositoryComment, UUID> {

    List<RepositoryComment> findByRepoId(UUID repoId);

    // Dodaj dodatkowe metody dostępu do repozytorium, jeśli są potrzebne
}
