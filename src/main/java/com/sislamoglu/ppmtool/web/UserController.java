package com.sislamoglu.ppmtool.web;

import com.sislamoglu.ppmtool.domain.User;
import com.sislamoglu.ppmtool.payload.JWTLoginSuccessResponse;
import com.sislamoglu.ppmtool.payload.LoginRequest;
import com.sislamoglu.ppmtool.security.JWTTokenProvider;
import com.sislamoglu.ppmtool.services.MapValidationErrorService;
import com.sislamoglu.ppmtool.services.UserService;
import com.sislamoglu.ppmtool.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.security.Principal;

import static com.sislamoglu.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap != null){
            return errorMap;
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        userValidator.validate(user, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if(errorMap != null)return errorMap;
        User newUser = userService.saveOrUpdateUser(user);

        return  new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/profile/{username}")
    public ResponseEntity<?> profileUser(@Valid @PathVariable("username") String username, Principal principal){
        User validUser = userService.getUser(username, principal.getName());
        return new ResponseEntity<User>(validUser, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/profile/{username}/edit")
    public ResponseEntity<?> updateProfileUser(@Valid @PathVariable("username") String username,
                                               @Valid @RequestBody User user , BindingResult bindingResult){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap != null)return errorMap;
        User newUser = userService.saveOrUpdateUser(user);
        return  new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
}
