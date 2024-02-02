package pk.reporank.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pk.reporank.backend.dto.response.ErrorMessage;
import pk.reporank.backend.dto.response.SuccessMessage;
import pk.reporank.backend.dto.response.ValidationError;
import pk.reporank.backend.model.AppUser;
import pk.reporank.backend.repository.UserRepository;
import pk.reporank.backend.service.ActivationService;

import java.util.Optional;

@RestController
@RequestMapping("/api/activate")
public class ActivationController {
    @Autowired

    private ActivationService activationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{activationToken}")
    @Operation(summary = "Aktywacja użytkownika",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik pomyślnie aktywowany",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "registered"))),
            @ApiResponse(responseCode = "400", description = "Błąd aktywacji",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class, example = "error"))),
    })
    public ResponseEntity<?> activateAccount(@PathVariable String activationToken) {
        Optional<AppUser> optionalUser = userRepository.findByActivationToken(activationToken);
        final ValidationError validationError = new ValidationError();
        validationError.setStatusCode(400);
        validationError.setError(null);

        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            if (!user.isActivated()) {

                activationService.activateAccount(user);
                System.out.println("Found user with activation token: " + activationToken);
                System.out.println("User details before activation: " + user);


                System.out.println("User details after activation: " + user);



                return ResponseEntity.ok(new SuccessMessage("Konto zostało aktywowane"));

            }
        } else
            validationError.getMessage().put("username", "Link Aktywacyjny jest niepoprawny, lub został już zużyty");
        return ResponseEntity
                .status(HttpStatus.valueOf(validationError.getStatusCode()))
                .body(validationError);

    }
}