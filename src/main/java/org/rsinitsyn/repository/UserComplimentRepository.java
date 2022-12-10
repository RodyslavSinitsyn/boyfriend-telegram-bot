package org.rsinitsyn.repository;

import java.util.Optional;
import org.rsinitsyn.entity.UserCompliment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserComplimentRepository extends JpaRepository<UserCompliment, Long> {

    Optional<UserCompliment> findByUserIdAndComplimentId(Long userId, Long complimentId);
}
