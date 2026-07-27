package de.mreinisch.backend.repository;

import de.mreinisch.backend.model.CD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CdRepo extends JpaRepository<CD, String> {
}
