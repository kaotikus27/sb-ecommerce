package com.ecommerce.sbecom.security.services;

import com.ecommerce.sbecom.model.User;
import com.ecommerce.sbecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException{
        User user = userRepository.findByUserName(username)
                .orElseThrow(()-> new UsernameNotFoundException(
                        "User not found with " + username));

        return UserDetailsImpl.build(user);
    }
}
