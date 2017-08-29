package com.abcbank.banking.user.servives;

import com.abcbank.banking.user.models.UserDTO;
import com.abcbank.banking.user.models.UserInfo;
import com.abcbank.banking.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         28/08/17
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        repository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserInfo create(UserDTO userDTO) {
        UserInfo u = new UserInfo();
        u.setName(userDTO.getName());
        u.setType(userDTO.getType());
        u.setUserName(userDTO.getUserName());

        u.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return repository.save(u);
    }

    @Override
    public UserInfo get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Page<UserInfo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findByUserName(userName);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userInfo.getType().name());

        User user = new User(userInfo.getUserName(), userInfo.getPassword(), Collections.singletonList(authority));
        logger.debug("Loaded user {} with role {}", user.getUsername(), user.getAuthorities().iterator().next());
        return user;
    }
}
