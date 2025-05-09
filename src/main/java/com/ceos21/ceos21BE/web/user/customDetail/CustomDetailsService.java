package com.ceos21.ceos21BE.web.user.customDetail;

import com.ceos21.ceos21BE.web.user.entity.User;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            User newUser = user.get();

            return new CustomDetails(newUser);
        }
        throw new  UsernameNotFoundException("User not found with email: " + email);
    }
}
