package pk.reporank.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pk.reporank.backend.dto.response.ErrorMessage;
import pk.reporank.backend.dto.response.SuccessMessage;
import pk.reporank.backend.model.Repo;
import pk.reporank.backend.model.RepoReaction;
import pk.reporank.backend.model.ReactionType;
import pk.reporank.backend.service.AppUserDetailsImpl;
import pk.reporank.backend.service.RepoReactionService;
import pk.reporank.backend.service.RepoService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    private final RepoReactionService repoReactionService;
    private final RepoService repoService;

    @Autowired
    public ReactionController(RepoReactionService repoReactionService, RepoService repoService) {
        this.repoReactionService = repoReactionService;
        this.repoService = repoService;
    }


    @Operation(summary = "Dodanie reakcji do repozytorium")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dodano reakcje",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "deleted"))),
            @ApiResponse(responseCode = "400", description = "Do tego repozytorium dodano już reakcje",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "not_found"))),
            @ApiResponse(responseCode = "404", description = "Repozytorium o podanym identyfikatorze nie znalezione",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "not_found"))),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "unauthorized"))),
    })
    @PostMapping("/add")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> addReactionToRepo(@RequestParam UUID repoId, @RequestParam ReactionType reactionType) {
        // Pobranie zalogowanego użytkownika
        UUID userId = getLoggedInUserId();
        if (userId == null) {
            return new ResponseEntity<>(new ErrorMessage(
                    401,
                    "Użytkownik nie zalogowany",
                    "Użytkownik nie zalogowany"),
                    HttpStatus.UNAUTHORIZED);
        }

        // Pobranie repozytorium
        Optional<Repo> repoOptional = repoService.getRepoByIdentifer(repoId);
        if (repoOptional.isPresent()) {
            Repo repo = repoOptional.get();

            // Sprawdzenie, czy użytkownik nie ocenił już tego repozytorium
            if (!repoReactionService.hasUserReactedToRepo(userId, repoId)) {
                // Dodanie reakcji do repozytorium
                RepoReaction repoReaction = new RepoReaction(userId, repoId, reactionType);
                repoReactionService.addReaction(repoReaction);

                repo.getReactions().add(repoReaction);
                repoService.updateRepo(repo);

                return new ResponseEntity<>(new SuccessMessage("Dodano reakcje"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage(
                        400,
                        "To repozytorium zostało już ocenione",
                        "To repozytorium zostało już ocenione"),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ErrorMessage(
                    404,
                    "Nie znaleziono repozytorium o podanym ID",
                    "Nie znaleziono repozytorium o podanym ID"),
                    HttpStatus.NOT_FOUND);
        }
    }

    private UUID getLoggedInUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUserDetailsImpl) {
            return ((AppUserDetailsImpl) principal).getId();
        }
        return null;
    }
}
