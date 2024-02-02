package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pk.reporank.backend.model.RepositoryComment;
import pk.reporank.backend.repository.RepositoryCommentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RepositoryCommentService {

    private final RepositoryCommentRepository commentRepository;

    @Autowired
    public RepositoryCommentService(RepositoryCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(RepositoryComment comment) {
        commentRepository.save(comment);
    }

    public List<RepositoryComment> getCommentsByRepoId(UUID repoId) {
        return commentRepository.findByRepoId(repoId);
    }

    // Dodaj dodatkowe metody serwisu, jeśli są potrzebne
}
