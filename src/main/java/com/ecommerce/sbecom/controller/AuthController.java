package com.ecommerce.sbecom.controller;

import com.ecommerce.sbecom.model.AppRole;
import com.ecommerce.sbecom.model.Role;
import com.ecommerce.sbecom.model.User;
import com.ecommerce.sbecom.repositories.UserRepository;
import com.ecommerce.sbecom.security.request.LoginRequest;
import com.ecommerce.sbecom.security.request.SignupRequest;
import com.ecommerce.sbecom.security.response.MessageResponse;
import com.ecommerce.sbecom.security.response.UserInfoResponse;
import com.ecommerce.sbecom.security.services.UserDetailsImpl;
import com.ecommerce.sbecom.sercurity.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@RestController
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken
                            (loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl  userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                jwtToken);

        return ResponseEntity.ok(response);
        }


        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(
                @Valid @RequestBody SignupRequest signUpRequest
        )
        {
            //checking if user exist
            //using user repository and the method -> existByUsername()
            if(userRepository.existsByUsername(signUpRequest.getUsername())){
                return ResponseEntity.badRequest().body(new MessageResponse(
                        "Error: Username is already taken!"
                ));
            }

            //check if user email exist
            //using user repository and the method -> existsByEmail()
            if(userRepository.existsByEmail(signUpRequest.getEmail())){
                return ResponseEntity.badRequest().body(new MessageResponse(
                        "Error: Email is already in use!"
                ));
            }

            // use and email is unique
            //create new user's account
            User user = new User(
                    signUpRequest.getUsername(),
                                 signUpRequest.getEmail(),
                                 encoder.encode(signUpRequest.getPassword())
                    );

            //-- accept "admin"
            //hold role string format
            Set<String> strRoles = signUpRequest.getRole();

            // accept ROLE_ADMIN
            // will hold the role for new user -- role entity format
            Set<Role> roles = new HashSet<>();


            //mapping the role into the request into the role
            if( strRoles == null){
                //user role default
                Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orEleseThrow(()-> new RuntimeException(
                                "Error: Role is not found."
                        ));
                roles.add(userRole);

            }else{
                //if the user pass role then it will throw it in role_admin
                //every role is mapped using switch case
                strRoles.forEach(role ->{
                   switch (role){
                       case "admin":
                           Role adminrole = roleRepository.findyByRoleName(AppRole.ROLE_ADMIN)
                                   .orElseThrow(()-> new RuntimeException(
                                           "Error: Role in not found."
                                   ));
                           roles.add(adminrole);
                           break;
                       case "seller":
                           Role modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                   .orElseThrow(()-> new RuntimeException(
                                           "Error: Role is not found."
                                   ));
                           roles.add(modRole);
                           break;
                       default:
                           Role userRole = roleRepository.findbyRoleName(AppRole.ROLE_USER)
                                   .orElseThrow(()-> new RuntimeException(
                                           "Error: Role is not found"
                                   ));
                           roles.add(userRole);

                   }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User registered succesfully!"));

        }


}

