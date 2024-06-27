package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository
               .findByUsername(username)
               .map(UserDetailServiceImpl::map)
                .orElseThrow();
    }

    private static UserDetails map(com.ivan.Flowers.Shop.models.entities.User user) { return User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(List.of())
            .disabled(false)
            .build();
    }


//    private static UserDetails map(User user) {
//        return User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .authorities(List.of())
//                .disabled(false)
//                .build();
//    }
}
