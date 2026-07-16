package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdCollectionDTO;
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

    /**
     *
     * @param collectionDTO
     * @return
     */
    public CdCollection generateCollection(CdCollectionDTO collectionDTO){
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
        }
        return new CdCollection();
    }
}
