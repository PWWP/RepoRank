package pk.reporank.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pk.reporank.backend.dto.LoginDTO;
import pk.reporank.backend.dto.RegisterDTO;
import pk.reporank.backend.dto.response.*;
import pk.reporank.backend.model.AppUser;
import pk.reporank.backend.model.UserRole;
import pk.reporank.backend.repository.RoleRepository;
import pk.reporank.backend.repository.UserRepository;
import pk.reporank.backend.security.jwt.JwtUtils;
import pk.reporank.backend.service.ActivationService;
import pk.reporank.backend.service.AppUserDetailsImpl;
import pk.reporank.backend.service.EmailService;
import pk.reporank.backend.util.PasswordValidator;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private PasswordValidator passwordValidator;
    @Autowired
    private ActivationService activationService;

    @Autowired
    private EmailService emailService;
    @Autowired
    JwtUtils jwtUtils;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Operation(summary = "Autentykacja użytkownika",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zalogowano",
                    content = @Content(schema = @Schema(implementation = SuccessMessageWithData.class, example = "logged_in"))),
            @ApiResponse(responseCode = "400", description = "Błąd przetwarzania zapytania",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class, example = "username_exists"))),
            @ApiResponse(responseCode = "401", description = "Niepoprawny login lub hasło",
                    content = @Content(schema = @Schema(implementation = ValidationError.class, example = "username_exists")))})
    @PostMapping("/login")

    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            final ValidationError validationError = new ValidationError();
            validationError.setStatusCode(400);
            validationError.setError(null);
            validationError.getMessage().put("username", "Błędne dane uwierzytelniające");
            return ResponseEntity
                    .status(HttpStatus.valueOf(validationError.getStatusCode()))
                    .body(validationError);
        } catch (Exception ex) {
            // Inny rodzaj błędu podczas uwierzytelniania
            final ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setStatusCode(400);
            errorMessage.setMessage(ex.getMessage());
            errorMessage.setError(ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.valueOf(errorMessage.getStatusCode()))
                    .body(errorMessage);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AppUserDetailsImpl userDetails = (AppUserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final JwtResponse body = new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                new Date((new Date()).getTime() + jwtExpirationMs));
        final SuccessMessageWithData<JwtResponse> response = new SuccessMessageWithData<>();
        response.setData(body);
        response.setMessage("");
        response.setStatusCode(200);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Rejestracja użytkownika",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik pomyślnie zarejestrowany",
                    content = @Content(schema = @Schema(implementation = SuccessMessage.class, example = "registered"))),
            @ApiResponse(responseCode = "400", description = "Użytkownik o takiej nazwie użytkownika lub adresie E-Mail już istnieje.",
                    content = @Content(schema = @Schema(implementation = ValidationError.class, example = "username_exists"))),
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        final ValidationError validationError = new ValidationError();
        validationError.setStatusCode(400);
        validationError.setError(null);

        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            validationError.getMessage().put("username", "Podana nazwa użytkownika już istnieje");
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            validationError.getMessage().put("email", "Użytkownik o takim adresie E-Mail już istnieje!");
        }
        if (!registerDTO.getEmail().contains("@")) {
            validationError.getMessage().put("email", "Podano nieprawidłowy adres E-Mail");
        }
        String password = registerDTO.getPassword();
        ValidationError passwordValidationError = passwordValidator.validatePassword(password);
        validationError.getMessage().putAll(passwordValidationError.getMessage());

        if (!validationError.getMessage().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(validationError.getStatusCode()))
                    .body(validationError);
        }

        //todo walidacja siły hasła

        if (!validationError.getMessage().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(validationError.getStatusCode()))
                    .body(validationError);
        }

        // Create new user's account
        AppUser user = new AppUser(registerDTO.getUsername(),
                registerDTO.getEmail(),
                encoder.encode(registerDTO.getPassword()));
        // Set activation token and save the user
        String activationToken = UUID.randomUUID().toString();
        user.setActivationToken(activationToken);
        user.setActivated(false);
        userRepository.save(user);

        // Send activation link to the user (in an email or response message)
        String activationLink = "http://localhost:8078/api/activate/" + activationToken;
        emailService.sendActivationEmail(registerDTO.getEmail(), activationLink);

        return ResponseEntity.ok(new SuccessMessage(
                "Użytkownik pomyślnie zarejestrowany. Link aktywacyjny został wysłany."));
    }


    //        Set<String> strRoles = signUpRequest.getRole();
    Set<UserRole> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            UserRole userRole = roleRepository.findByName(EUserRole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        UserRole adminRole = roleRepository.findByName(EUserRole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
//                    case "mod":
//                        UserRole modRole = roleRepository.findByName(EUserRole.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//
//                        break;
//                    default:
//                        UserRole userRole = roleRepository.findByName(EUserRole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }


}