package pk.reporank.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pk.reporank.backend.dto.response.ErrorMessage;
import pk.reporank.backend.dto.response.SuccessMessage;
import pk.reporank.backend.dto.response.SuccessMessageWithData;
import pk.reporank.backend.model.Repo;
import pk.reporank.backend.model.RepositoryAuthor;
import pk.reporank.backend.service.AppUserDetailsImpl;
import pk.reporank.backend.service.AppUserService;
import pk.reporank.backend.service.RepoService;
import pk.reporank.backend.service.RepositoryAuthorService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/repositories")
public class RepositoryController {

    private final RepoService RepoService;
    private final AppUserService appUserService;

    private final RepositoryAuthorService repositoryAuthorService;


    public RepositoryController(RepoService RepoService, AppUserService appUserService, RepositoryAuthorService repositoryAuthorService) {
        this.RepoService = RepoService;
        this.appUserService = appUserService;
        this.repositoryAuthorService = repositoryAuthorService;
    }

    @PostMapping("/create")
    @Operation(summary = "Tworzenie nowego repozytorium")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stworzono repozytorium",
                    content = @Content(schema = @Schema(implementation = SuccessMessageWithData.class, example = "created"))),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "unauthorized"))),
            @ApiResponse(responseCode = "400", description = "Błąd tworzenia repozytorium",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "bad_request"))),
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createRepository(@RequestBody Repo repo) {
        // Pobranie zalogowanego użytkownika
        AppUserDetailsImpl createdBy = getLoggedInUser();
        if (createdBy == null) {
            return new ResponseEntity<>(new ErrorMessage(
                    401,
                    "Użytkownik nie zalogowany",
                    "Użytkownik nie zalogowany"),
                    HttpStatus.UNAUTHORIZED);
        }

        // Ustawienie osoby dodającej repozytorium
        repo.setCreatedBy(appUserService.getUserById(createdBy.getId()));
        repo.setAddTime(System.currentTimeMillis());
        System.out.println("ID wprowadzającego: "+ repo.getCreatedBy().getId());

        // Ustawienie autora

        for(RepositoryAuthor author : repo.getAuthors()) {
            author.setId(UUID.randomUUID());
            repositoryAuthorService.save(author);
        }

        repo.setReactions(new ArrayList<>());
        repo.setComments(new ArrayList<>());

        // Zapisanie repozytorium
        Repo createdRepo = RepoService.createRepo(repo);
        if (createdRepo != null) {
            return new ResponseEntity<>(new SuccessMessageWithData<>(createdRepo, 201), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ErrorMessage(
                    400,
                    "Błąd tworzenia repozytorium",
                    "Błąd tworzenia repozytorium"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{repositoryId}")
    @Operation(summary = "Pobieranie repozytorium o danym identyfikatorze")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zwraca repozytorium",
                    content = @Content(schema = @Schema(implementation = SuccessMessageWithData.class, example = "logged_in"))),
            @ApiResponse(responseCode = "404", description = "Repozytorium o podanym identyfikatorze nie znalezione",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "not_found"))),
    })
    public ResponseEntity<?> getRepositoryById(@PathVariable UUID repositoryId) {
        Repo repo = RepoService.getRepoById(repositoryId);
        if (repo != null) {
            return new ResponseEntity<>(new SuccessMessageWithData<>(repo, 200), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMessage(
                    404,
                    "Repozytorium o podanym identyfikatorze nie znalezione",
                    "Repozytorium o podanym identyfikatorze nie znalezione"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listowanie wszystkich repozytoriów")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zwraca listę repozytoriów",
                    content = @Content(schema = @Schema(implementation = SuccessMessageWithData.class, example = "logged_in"))),
    })
    public ResponseEntity<?> getAllRepositories() {
        List<Repo> repositories = RepoService.getAllRepos();
        return new ResponseEntity<>(new SuccessMessageWithData<>(repositories, 200), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{repositoryId}")
    @Operation(summary = "Usuwanie repozytorium o danym identyfikatorze")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usunięto repozytorium",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "deleted"))),
            @ApiResponse(responseCode = "404", description = "Repozytorium o podanym identyfikatorze nie znalezione",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "not_found"))),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "unauthorized"))),
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteRepository(@PathVariable UUID repositoryId) {
        // Pobranie zalogowanego użytkownika
        AppUserDetailsImpl createdBy = getLoggedInUser();
        if (createdBy == null) {
            return new ResponseEntity<>(new ErrorMessage(
                    401,
                    "Użytkownik nie zalogowany",
                    "Użytkownik nie zalogowany"),
                    HttpStatus.UNAUTHORIZED);
        }

        // Usunięcie repozytorium
        boolean deleted = RepoService.deleteRepo(repositoryId, createdBy.getId());
        if (deleted) {
            return new ResponseEntity<>(new SuccessMessage("Usunięto repozytorium"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMessage(
                    404,
                    "Repozytorium o podanym identyfikatorze nie znalezione lub brak uprawnień",
                    "Repozytorium o podanym identyfikatorze nie znalezione lub brak uprawnień"),
                    HttpStatus.NOT_FOUND);
        }
    }

    private AppUserDetailsImpl getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUserDetailsImpl) {
            return (AppUserDetailsImpl) principal;
        }
        return null;
    }
}
