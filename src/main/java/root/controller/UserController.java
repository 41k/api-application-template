package root.controller;

import root.dto.UserDto;
import root.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static root.controller.AccessTokenAuthenticationFilter.ACCESS_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/users/{id}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDto get(@RequestHeader(ACCESS_TOKEN_HEADER) String accessToken,
                       @PathVariable String id) {
        return userService.getUser(id);
    }
}
