package root.controller;

import root.dto.UserDto;
import root.dto.UserUpdateDto;
import root.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static root.controller.AccessTokenAuthenticationFilter.ACCESS_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    @GetMapping
    public UserDto get(@RequestHeader(ACCESS_TOKEN_HEADER) String accessToken) {
        return userService.getMe();
    }

    @PutMapping
    public void update(@RequestHeader(ACCESS_TOKEN_HEADER) String accessToken,
                       @RequestBody @Valid UserUpdateDto dto) {
        userService.updateMe(dto);
    }

    @DeleteMapping
    public void deactivate(@RequestHeader(ACCESS_TOKEN_HEADER) String accessToken) {
        userService.deactivateMe();
    }
}
