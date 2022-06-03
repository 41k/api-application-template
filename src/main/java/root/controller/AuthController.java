package root.controller;

import root.dto.ResetPasswordDto;
import root.dto.SignInDto;
import root.dto.UserActivationDto;
import root.dto.UserRegistrationDto;
import root.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/registration/step-1")
    public void registerUser(@RequestBody @Valid UserRegistrationDto dto) {
        userService.registerUser(dto);
    }

    @PostMapping("/registration/step-2")
    public void activateUser(@RequestBody @Valid UserActivationDto dto) {
        userService.activateUser(dto);
    }

    @PostMapping("/sign-in")
    public String signIn(@RequestBody @Valid SignInDto dto) {
        return userService.signIn(dto);
    }

    @PostMapping("/reset-password")
    public void signIn(@RequestBody @Valid ResetPasswordDto dto) {
        userService.resetPassword(dto);
    }
}
