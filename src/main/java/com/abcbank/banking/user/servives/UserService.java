package com.abcbank.banking.user.servives;

import com.abcbank.banking.user.models.UserInfo;
import com.abcbank.banking.user.models.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         28/08/17
 */
public interface UserService extends UserDetailsService {

    UserInfo create(UserDTO userDTO);

    UserInfo get(Long id);

    Page<UserInfo> list(Pageable pageable);
}
