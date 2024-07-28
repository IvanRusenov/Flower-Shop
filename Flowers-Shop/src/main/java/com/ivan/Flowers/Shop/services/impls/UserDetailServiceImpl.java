package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(
                        () -> new ObjectNotFoundException("User not found", User.class.getSimpleName())
                );
    }

    private static UserDetails map(com.ivan.Flowers.Shop.models.entities.User user) {

        return new ShopUserDetails(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().getType().name()))




        );
    }

    private static GrantedAuthority map(RoleType roleType) {
        return new SimpleGrantedAuthority(roleType.name());
    }

}
