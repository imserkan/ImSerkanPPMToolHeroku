package com.sislamoglu.ppmtool.services;

import com.sislamoglu.ppmtool.domain.UserProfile;
import com.sislamoglu.ppmtool.exceptions.UserProfileNotFoundException;
import com.sislamoglu.ppmtool.repositories.UserProfileRepository;
import com.sislamoglu.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public UserProfile saveOrUpdateUserProfile(UserProfile userProfile){
        if(userProfile.getId() != null){
            UserProfile existingUserProfile = userProfileRepository.getById(userProfile.getId());
            if(existingUserProfile == null){
                throw new UserProfileNotFoundException("The user profile with ID: " + userProfile.getId() + " does not found!.");
            }
        }
        return userProfileRepository.save(userProfile);
    }

    public UserProfile findUserProfileByUsername(String username){
        return userProfileRepository.getByUsername(username);
    }

}
