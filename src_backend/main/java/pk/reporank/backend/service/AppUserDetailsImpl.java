package pk.reporank.backend.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pk.reporank.backend.model.AppUser;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class AppUserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public AppUserDetailsImpl(UUID id, String username, String email, String password,
                              boolean activated, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static AppUserDetailsImpl build(AppUser appUser) {
        List<GrantedAuthority> authorities = appUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new AppUserDetailsImpl(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getEmail(),
                appUser.getPassword(),
                appUser.isActivated(),  // Use the isActivated method to check if the account is activated
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;//todo
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;//todo
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;//todo
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
