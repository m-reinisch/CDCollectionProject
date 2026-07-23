package de.mreinisch.backend.repository;

import de.mreinisch.backend.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepo extends JpaRepository<Track, Long> {
}
