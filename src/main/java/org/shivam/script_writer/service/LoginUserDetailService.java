package org.shivam.script_writer.service;

import org.shivam.script_writer.entity.User;
import org.shivam.script_writer.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailService implements UserDetailsService{

    private final UserRepository userRepository;

    public LoginUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User User = userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UsernameNotFoundException("Email not found"));
        return new UserPrincipal(User);
    }
}
