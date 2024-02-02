package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pk.reporank.backend.model.RepoReaction;
import pk.reporank.backend.repository.RepoReactionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RepoReactionService {

    private final RepoReactionRepository repoReactionRepository;

    @Autowired
    public RepoReactionService(RepoReactionRepository repoReactionRepository) {
        this.repoReactionRepository = repoReactionRepository;
    }

    public void addReaction(RepoReaction repoReaction) {
        repoReactionRepository.save(repoReaction);
    }

    public boolean hasUserReactedToRepo(UUID userId, UUID repoId) {
        return repoReactionRepository.existsByUserIdAndRepoId(userId, repoId);
    }

    public List<RepoReaction> getReactionsByRepoId(UUID repoId) {
        return repoReactionRepository.findByRepoId(repoId);
    }

    // Dodaj dodatkowe metody serwisu, jeśli są potrzebne
}
