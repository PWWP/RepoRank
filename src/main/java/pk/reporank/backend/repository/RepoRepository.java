package pk.reporank.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import pk.reporank.backend.model.Repo;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
@Repository
public interface RepoRepository extends JpaRepository<Repo, UUID> {

    boolean existsByName(String name);

    List<Repo> findByNameContainingIgnoreCase(String name);

    // Dodaj dodatkowe metody dostępu do repozytorium, jeśli są potrzebne
}
