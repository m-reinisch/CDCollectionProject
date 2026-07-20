package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdCollectionDTO;
import de.mreinisch.backend.exception.AppUserNotFound;
import de.mreinisch.backend.exception.CdCollectionNotFound;
import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.model.CD;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.repository.AppUserRepo;
import de.mreinisch.backend.repository.CdCollectionRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CollectionService {
    private final CdCollectionRepo repo;
    private final IdService idService;
    private final AppUserRepo userRepo;

    public CollectionService(CdCollectionRepo repo, IdService idService, AppUserRepo userRepo) {
        this.repo = repo;
        this.idService = idService;
        this.userRepo = userRepo;
    }

    /** Crearte a new CD Collection and saves it
     *
     * @param collectionDTO name and user for new collection
     * @return saved CD Collection
     * @throws  AppUserNotFound when User not in database
     */
    public CdCollection generateCollection(CdCollectionDTO collectionDTO) throws AppUserNotFound {
        CdCollection newCdCollection;
        String id= idService.generateId();
        AppUser collOwner= collectionDTO.appUser();
        List<CD> cdList= Collections.emptyList();

        if (userRepo.findAppUserById(collOwner.getId()) != null){
            newCdCollection= new CdCollection(id,
                                              collectionDTO.name(),
                                              collOwner,
                                              cdList);
            repo.save(newCdCollection);
            return newCdCollection;
        } else {
            throw new AppUserNotFound("Benutzer mit id: " +
                                      collOwner.getId() +
                                      " wurde nicht gefunden!");
        }
    }

    /** Returns all CD collections of an app user.
     *
     * @param userId to search for
     * @return List of collections
     * @throws  AppUserNotFound when User not in database
     */
    public List<CdCollection> getAllCdCollectionsByAppUserId(String userId) throws AppUserNotFound {
        if (userRepo.findAppUserById(userId) != null) {
            return repo.findAllByAppUser_Id(userId);
        } else {
            throw new AppUserNotFound("Benutzer mit id: " + userId +
                                      " wurde nicht gefunden!");
        }
    }

    /** Deletes the CD collection from the database.
     *
     * @param id to search for
     * @return true, if deleted
     * @throws  CdCollectionNotFound when Collection not in database
     */
    public Boolean removeCdCollection(String id) throws CdCollectionNotFound {
        CdCollection delColl= repo.findById(id).orElse(null);

        if (delColl != null) {
            return repo.deleteCdCollectionsById(id);
        } else {
            throw new CdCollectionNotFound("Sammlung mit id: " + id +
                                           " wurde nicht gefunden!");
        }
    }
}
