package com.abcbank.banking.user.repositories;

import com.abcbank.banking.user.models.UserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         28/08/17
 */
public interface UserRepository extends PagingAndSortingRepository<UserInfo, Long> {
    UserInfo findByUserName(String userName);
}
