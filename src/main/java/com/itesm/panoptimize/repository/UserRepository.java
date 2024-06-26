package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Get all agents using pagination
     * @return List of agents
     */
    @Query("SELECT u FROM User u INNER JOIN u.userType ut WHERE ut.typeName = :type")
    Page<User> getUsersByType(String type, Pageable pageable);

    /**
     * Get users with a specific amazon connect id
     * @param amazonConnectId Amazon connect id
     */
    Optional<User> connectId(String amazonConnectId);

    /**
     * Get users with a specific firebase id
     * @param firebaseId Firebase id
     * @return Optional of user
     */
    Optional<User> firebaseId(String firebaseId);
}
