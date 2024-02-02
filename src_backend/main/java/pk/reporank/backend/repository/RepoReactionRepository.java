package pk.reporank.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.reporank.backend.model.RepoReaction;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepoReactionRepository extends JpaRepository<RepoReaction, UUID> {

    boolean existsByUserIdAndRepoId(UUID userId, UUID repoId);

    List<RepoReaction> findByRepoId(UUID repoId);

    // Dodaj dodatkowe metody repozytorium, jeśli są potrzebne
}
