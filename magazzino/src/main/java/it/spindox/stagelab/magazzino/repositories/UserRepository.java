package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsernameAndPassword(String username, String password);
}