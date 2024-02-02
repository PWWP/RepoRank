package pk.reporank.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.reporank.backend.dto.response.ErrorMessage;
import pk.reporank.backend.dto.response.SuccessMessage;
import pk.reporank.backend.service.AppUserService;


@RestController
@RequestMapping("/api/user")
public class UserController {
    //todo ustawienie lokalizacji gracza

    private final AppUserService userService;

    public UserController(AppUserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Aktualizacja hasła")
    @PostMapping("/changePassword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zwraca wiadomość o sukcesie",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "logged_in"))),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "unauthorized"))),})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> changePassword(@RequestBody String oldPassword, @RequestBody String newPassword) {
        //todo
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Aktualizacja danych użytkowników")
    @PostMapping("/updateUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zwraca wiadomość o sukcesie",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "logged_in"))),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "unauthorized"))),})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> changePassword(@RequestBody String description) {
        //todo
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Usunięcie użytkownika")
    @DeleteMapping("/deleteUser/{userID}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zwraca wiadomość o sukcesie",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "logged_in"))),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "unauthorized"))),})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteUser(@RequestBody String location) {
        //todo
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
