package pk.reporank.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.reporank.backend.model.AppUser;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByEmail(String email);


    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<AppUser> findByActivationToken(String activationToken);
    Optional<AppUser> findByResetToken(String resetToken);

}
