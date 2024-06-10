package root.controller;

import root.dto.UserDto;
import root.dto.search.PageableSearchResult;
import root.dto.search.SearchContext;
import root.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static root.controller.AccessTokenAuthenticationFilter.ACCESS_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@RequestHeader(ACCESS_TOKEN_HEADER) String accessToken,
                       @PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping("/search")
    public PageableSearchResult<UserDto> search(@RequestHeader(ACCESS_TOKEN_HEADER) String accessToken,
                                                @RequestBody @Valid SearchContext searchContext) {
        return userService.search(searchContext);
    }
}
