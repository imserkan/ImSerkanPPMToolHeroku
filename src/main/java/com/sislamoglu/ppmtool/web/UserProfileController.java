package com.sislamoglu.ppmtool.web;

import com.sislamoglu.ppmtool.domain.User;
import com.sislamoglu.ppmtool.domain.UserProfile;
import com.sislamoglu.ppmtool.services.MapValidationErrorService;
import com.sislamoglu.ppmtool.services.UserProfileService;
import com.sislamoglu.ppmtool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public ResponseEntity<?> profileUser(@PathVariable("username") String username, Principal principal){
        UserProfile validUserProfile = userProfileService.findUserProfileByUsername(principal.getName());
        return new ResponseEntity<UserProfile>(validUserProfile, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{username}/edit")
    public ResponseEntity<?> updateProfileUser(@PathVariable("username") String username,
                                               @Valid @RequestBody UserProfile userProfile ,
                                               BindingResult bindingResult){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap != null)return errorMap;
        UserProfile newUserProfile = userProfileService.saveOrUpdateUserProfile(userProfile);
        return  new ResponseEntity<UserProfile>(newUserProfile, HttpStatus.CREATED);
    }
}
