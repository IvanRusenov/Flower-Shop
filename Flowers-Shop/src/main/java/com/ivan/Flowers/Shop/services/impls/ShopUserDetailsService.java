package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ShopUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(ShopUserDetailsService::map)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + username + " not found")
                );
    }

    private static UserDetails map(User user) {

        return new ShopUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(Role::getType)
                        .map(ShopUserDetailsService::map)
                        .toList()
        );
    }

    private static GrantedAuthority map(RoleType roleType) {
        return new SimpleGrantedAuthority(roleType.name());
    }
}
