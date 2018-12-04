package com.sislamoglu.ppmtool.repositories;

import com.sislamoglu.ppmtool.domain.User;
import com.sislamoglu.ppmtool.domain.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    UserProfile getById(Long id);
    UserProfile getByUsername(String username);
}
