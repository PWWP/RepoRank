package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pk.reporank.backend.model.Repo;
import pk.reporank.backend.repository.RepoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RepoService {

    private final RepoRepository repoRepository;

    @Autowired
    public RepoService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public Repo createRepo(Repo repo) {
        // Dodaj logikę walidacji, jeśli potrzebna
        return repoRepository.save(repo);
    }


    public Repo updateRepo(Repo repo) {
        return repoRepository.save(repo);
    }

    public Repo getRepoById(UUID repoId) {
        Optional<Repo> repoOptional = repoRepository.findById(repoId);
        return repoOptional.orElse(null);
    }

    public Optional<Repo> getRepoByIdentifer(UUID repoId) {
        return repoRepository.findById(repoId);
    }


    public List<Repo> getAllRepos() {
        return repoRepository.findAll();
    }

    public boolean deleteRepo(UUID repoId, UUID ownerId) {
        Optional<Repo> repoOptional = repoRepository.findById(repoId);
        if (repoOptional.isPresent()) {
            Repo repo = repoOptional.get();

            // Sprawdzenie, czy użytkownik o podanym ownerId jest właścicielem repozytorium
            if (repo.getCreatedBy() != null && repo.getCreatedBy().getId().equals(ownerId)) {
                repoRepository.deleteById(repoId);
                return true;
            }
        }
        return false;
    }

    public List<Repo> findReposByName(String name) {
        return repoRepository.findByNameContainingIgnoreCase(name);
    }

    // Dodaj dodatkowe metody serwisu, jeśli są potrzebne
}
