package ru.faimon.instazoo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.faimon.instazoo.payload.request.LoginRequest;
import ru.faimon.instazoo.payload.request.SignUpRequest;
import ru.faimon.instazoo.payload.response.JWTTokenSuccessResponse;
import ru.faimon.instazoo.payload.response.MessageResponse;
import ru.faimon.instazoo.security.JWTAuthenticationFilter;
import ru.faimon.instazoo.security.JWTTokenProvider;
import ru.faimon.instazoo.security.SecurityConstants;
import ru.faimon.instazoo.service.UserService;
import ru.faimon.instazoo.validations.ResponseErrorValidation;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    public static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,
                                               BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

}
