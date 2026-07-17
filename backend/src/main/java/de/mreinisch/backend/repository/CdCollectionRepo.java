package de.mreinisch.backend.repository;

import de.mreinisch.backend.model.CdCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CdCollectionRepo extends JpaRepository<CdCollection, String> {
    List<CdCollection> findAllByAppUser_Id(String appUserId);
    Boolean deleteCdCollectionsById(String id);
}
