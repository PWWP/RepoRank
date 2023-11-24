package pk.reporank.backend.contoller;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pk.reporank.backend.dto.LoginDTO;
import pk.reporank.backend.dto.RegisterDTO;
import pk.reporank.backend.dto.response.ErrorResponse;
import pk.reporank.backend.dto.response.JwtResponse;
import pk.reporank.backend.dto.response.MessageResponse;
import pk.reporank.backend.model.AppUser;
import pk.reporank.backend.model.UserRole;
import pk.reporank.backend.repository.RoleRepository;
import pk.reporank.backend.repository.UserRepository;
import pk.reporank.backend.security.jwt.JwtUtils;
import pk.reporank.backend.service.AppUserDetailsImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    JwtUtils jwtUtils;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Operation(summary = "Autentykacja użytkownika",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zalogowano",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class, example = "logged_in"))),
            @ApiResponse(responseCode = "400", description = "Niepoprawny login lub hasło",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class, example = "username_exists")))})
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AppUserDetailsImpl userDetails = (AppUserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                new Date((new Date()).getTime() + jwtExpirationMs)));
    }

    @PostMapping("/register")
    @Operation(summary = "Rejestracja użytkownika",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik pomyślnie zarejestrowany",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class, example = "registered"))),
            @ApiResponse(responseCode = "400", description = "Użytkownik o takiej nazwie użytkownika lub adresie E-Mail już istnieje.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class, example = "username_exists")))})
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .body(new MessageResponse("Użytkownik o takiej nazwie użytkownika już istnieje!"));
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .body(new MessageResponse("Użytkownik o takim e-mailu już istnieje!"));
        }

        // Create new user's account
        AppUser user = new AppUser(registerDTO.getUsername(),
                registerDTO.getEmail(),
                encoder.encode(registerDTO.getPassword()));

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

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Użytkownik pomyślnie zarejestrowany"));
    }
}