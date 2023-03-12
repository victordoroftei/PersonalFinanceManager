package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.domain.entity.UserPrincipal;
import com.personalfinancemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * method that searches a user by email and, if it finds it, also check the password to see if it's correct
     * @param email - email of the user that wants to log in
     * @return - UserPrincipal object, representing the logged user, if the authentication succeeds
     * @throws UsernameNotFoundException - if the user doesn't exist
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByEmailAddress(email).orElse(null);

        if(user == null) {
            throw new UsernameNotFoundException("Email not found!");
        }
        return new UserPrincipal(user);
    }

    public UserEntity getCurrentlyLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserPrincipal) {
            Integer id = ((UserPrincipal) principal).getId();
            return userRepository.findById(id).orElse(null);
        }
        throw new EntityNotFoundException("Could not get the currently logged user!");
    }
}
