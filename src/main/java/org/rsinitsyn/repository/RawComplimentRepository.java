package org.rsinitsyn.repository;

import java.util.Optional;
import java.util.UUID;
import org.rsinitsyn.entity.RawCompliment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawComplimentRepository extends JpaRepository<RawCompliment, UUID> {

    Optional<RawCompliment> findByText(String text);
}
