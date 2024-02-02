package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.reporank.backend.model.AppUser;
import pk.reporank.backend.repository.UserRepository;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    final
    UserRepository userRepository;

    public AppUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika o nazwie " + username));

        if (!user.isActivated()) {
            throw new DisabledException("Konto nie zostało jeszcze aktywowane. Sprawdź swoją skrzynkę pocztową.");
        }
        return AppUserDetailsImpl.build(user);
    }

}