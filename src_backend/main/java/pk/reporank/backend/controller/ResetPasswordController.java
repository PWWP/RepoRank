package pk.reporank.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pk.reporank.backend.dto.response.SuccessMessage;
import pk.reporank.backend.dto.response.ValidationError;
import pk.reporank.backend.exception.TokenExpiredException;
import pk.reporank.backend.exception.TokenNotFoundException;
import pk.reporank.backend.service.PasswordResetService;
import pk.reporank.backend.util.PasswordValidator;

@RestController
@RequestMapping("/api/reset-password")
public class ResetPasswordController {

    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    private PasswordValidator passwordValidator;


    @Operation(summary = "Reset hasła",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomyślnie wysłano link",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "registered"))),
            @ApiResponse(responseCode = "400", description = "Błąd resetu hasła np. niepoprawny email",
                    content = @Content(schema = @Schema(implementation = ValidationError.class, example = "error"))),
    })
    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        if (passwordResetService.requestPasswordReset(email)) {
            return ResponseEntity.ok(new SuccessMessage("Na podany adres E-Mail został wysłany link do resetu hasła"));
        } else {
            final ValidationError body = new ValidationError();
            body.getMessage().put("email", "Nie znaleziono adresu E-Mail");
            body.setStatusCode(400);
            body.setError(null);
            return ResponseEntity
                    .status(HttpStatus.valueOf(body.getStatusCode()))
                    .body(body);
        }
    }

    @PostMapping("/confirm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hasło pomyślnie zresetowane",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "registered"))),
            @ApiResponse(responseCode = "400", description = "Błąd resetu hasła",
                    content = @Content(schema = @Schema(implementation = ValidationError.class, example = "error"))),
    })
    public ResponseEntity<?> confirmResetPassword(@RequestParam String token, @RequestParam String newPassword) {
        final ValidationError validationError = passwordValidator.validatePassword(newPassword);

        if (!validationError.getMessage().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(validationError.getStatusCode()))
                    .body(validationError);
        }

        try {
            passwordResetService.confirmPasswordReset(token, newPassword);
            return ResponseEntity.ok(new SuccessMessage("Hasło zostało zresetowane. Możesz się teraz zalogować."));
        } catch (TokenExpiredException ex) {
            validationError.getMessage().put("token", "Ważność tokenu resetującego hasło skończyła się!");
        } catch (TokenNotFoundException ex) {
            validationError.getMessage().put("token", "Niepoprawny token!");
        }

        return ResponseEntity
                .status(HttpStatus.valueOf(validationError.getStatusCode()))
                .body(validationError);
        }

}