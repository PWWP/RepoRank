package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pk.reporank.backend.model.RepositoryAuthor;
import pk.reporank.backend.repository.RepositoryAuthorRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class RepositoryAuthorService {

    private final RepositoryAuthorRepository repositoryAuthorRepository;

    @Autowired
    public RepositoryAuthorService(RepositoryAuthorRepository repositoryAuthorRepository) {
        this.repositoryAuthorRepository = repositoryAuthorRepository;
    }

    public Optional<RepositoryAuthor> findByUsername(String username) {
        return repositoryAuthorRepository.findByUsername(username);
    }

    public RepositoryAuthor save(RepositoryAuthor repositoryAuthor) {
        return repositoryAuthorRepository.save(repositoryAuthor);
    }

    public Optional<RepositoryAuthor> findById(UUID id) {
        return repositoryAuthorRepository.findById(id);
    }
}
