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
import pk.reporank.backend.model.RepositoryComment;
import pk.reporank.backend.service.AppUserDetailsImpl;
import pk.reporank.backend.service.RepoService;
import pk.reporank.backend.service.RepositoryCommentService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final RepositoryCommentService commentService;
    private final RepoService repoService;

    @Autowired
    public CommentController(RepositoryCommentService commentService, RepoService repoService) {
        this.commentService = commentService;
        this.repoService = repoService;
    }

    @Operation(summary = "Dodanie komentarza do repozytorium")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dodano komentarz",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "deleted"))),
            @ApiResponse(responseCode = "404", description = "Repozytorium o podanym identyfikatorze nie znalezione",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "not_found"))),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "unauthorized"))),
    })
    @PostMapping("/add")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> addCommentToRepo(@RequestParam UUID repoId, @RequestParam String content) {
        // Pobranie zalogowanego użytkownika
        UUID userId = getLoggedInUserId();
        if (userId == null) {
            return new ResponseEntity<>(new ErrorMessage(
                    401,
                    "Użytkownik nie zalogowany",
                    "Użytkownik nie zalogowany"),
                    HttpStatus.NOT_FOUND);
        }

        // Pobranie repozytorium
        Optional<Repo> repoOptional = repoService.getRepoByIdentifer(repoId);
        if (repoOptional.isPresent()) {
            Repo repo = repoOptional.get();

            // Dodanie komentarza do repozytorium
            RepositoryComment comment = new RepositoryComment(userId, repoId, content);
            commentService.addComment(comment);

            repo.getComments().add(comment);
            repoService.updateRepo(repo);

            return new ResponseEntity<>(new SuccessMessage("Dodano nowy komentarz"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMessage(
                    404,
                    "Nie znaleziono repozytorium o podanym identyfikatorze",
                    "Nie znaleziono repozytorium o podanym identyfikatorze"),
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
