package com.sislamoglu.ppmtool.services;

import com.sislamoglu.ppmtool.domain.User;
import com.sislamoglu.ppmtool.exceptions.UserNotFoundException;
import com.sislamoglu.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.sislamoglu.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveOrUpdateUser(User user){
        if (user.getId()!= null){
            User existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser==null){
                throw new UserNotFoundException("The user with name '" + user.getUsername() + "' is not found.");
            }
        }
        try{
            //Saving the User
            if(user.getId() == null){
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setUsername(user.getUsername());
                user.setConfirmPassword("");
            }
            //Updating the User
            else{
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setUsername(user.getUsername());
                user.setConfirmPassword("");
                user.setFullname(user.getFullname());
                user.setDepartment(user.getDepartment());
                user.setHire_date(user.getHire_date());
                user.setBirth_date(user.getBirth_date());
                user.setGender(user.getGender());
                user.setAddress(user.getAddress());
                user.setPhoneNumber(user.getPhoneNumber());
            }
            return userRepository.save(user);

        }catch (Exception ex){
            throw new UsernameAlreadyExistsException("Username '"+ user.getUsername()+"' already exists.");
        }
    }
    public User getUser(String username, String realUser){
        System.out.println(username + "'s profile is searched from " + realUser);
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException("Profile '" + username + "' is not found.");
        }
        return user;
    }

}
