package root.service.mapper;

import org.mapstruct.Mapper;
import root.dto.UserDto;
import root.model.User;

// NOTE: project should be re-compiled in order to have mapper implementation in classpath. Run:
// mvn -DskipTests=true clean verify
@Mapper
public interface UserMapper {

    UserDto toDto(User user);
}
