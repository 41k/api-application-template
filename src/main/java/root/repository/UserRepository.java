package root.repository;

import root.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndActive(String email, boolean active);

    Optional<User> findByIdAndActive(String id, boolean active);
}
