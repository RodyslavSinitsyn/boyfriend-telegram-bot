package org.rsinitsyn.repository;

import java.util.Optional;
import org.rsinitsyn.entity.RawCompliment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawComplimentRepository extends JpaRepository<RawCompliment, Long> {

    Optional<RawCompliment> findByText(String text);
}
