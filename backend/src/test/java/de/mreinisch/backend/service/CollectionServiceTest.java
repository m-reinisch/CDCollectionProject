package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdCollectionDTO;
import de.mreinisch.backend.exception.AppUserNotFound;
import de.mreinisch.backend.exception.CdCollectionNotFound;
import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.repository.AppUserRepo;
import de.mreinisch.backend.repository.CdCollectionRepo;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionServiceTest {
    @Test
    void generateCollection_shouldReturnCdCollection_whenSaved() throws AppUserNotFound {
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollectionDTO cdCollection=
                new CdCollectionDTO("Testsammlung", appUser);
        CdCollection expected= new CdCollection(id,
                                          "Testsammlung",
                                                appUser,
                                                Collections.emptyList());
        CdCollection actual;

        when(mockingIdService.generateId()).thenReturn(id);
        when(mockUserRepo.findAppUserById(id)).thenReturn(appUser);
        when(mockRepo.save(expected)).thenReturn(expected);
        actual= service.generateCollection(cdCollection);
        assertEquals(expected, actual);
    }

    @Test
    void generateCollection_shouldThrowException_whenAppUserNotFound(){
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                mockingIdService,
                mockUserRepo);
        String id= "0";
        AppUser appUser= new AppUser(id, "FailUser");
        CdCollectionDTO cdCollection=
                new CdCollectionDTO("Testsammlung", appUser);

        assertThatExceptionOfType(AppUserNotFound.class)
                .isThrownBy( () ->
                        service.generateCollection(cdCollection))
                .withMessage("Benutzer mit id: " + id +
                             " wurde nicht gefunden!");
    }

    @Test
    void getAllCdCollectionsByAppUserId_shouldReturnEmptyList_whenDatabaseIsEmpty() throws AppUserNotFound {
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String uid= "0";
        AppUser appUser= new AppUser(uid, "TestUser");
        List<CdCollection> expected= Collections.emptyList();
        List<CdCollection> actual;

        when(mockUserRepo.findAppUserById(uid)).thenReturn(appUser);
        actual= service.getAllCdCollectionsByAppUserId(uid);
        assertEquals(expected, actual);
    }

    @Test
    void getAllCdCollectionsByAppUserId_shouldReturnCdCollectionList_whenDatabaseNotEmpty() throws AppUserNotFound {
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String uid= "0";
        AppUser appUser= new AppUser(uid, "TestUser");
        CdCollection cdCollection= new CdCollection(uid,
                                             "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        List<CdCollection> expected= List.of(cdCollection);
        List<CdCollection> actual;

        when(mockUserRepo.findAppUserById(uid)).thenReturn(appUser);
        when(mockRepo.findAllByAppUser_Id(uid)).thenReturn(expected);
        actual= service.getAllCdCollectionsByAppUserId(uid);
        assertEquals(expected, actual);
    }

    @Test
    void getAllCdCollectionsByAppUserId_shouldThrowException_whenAppUserNotFound(){
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String uid= "0";

        assertThatExceptionOfType(AppUserNotFound.class)
                .isThrownBy( () ->
                        service.getAllCdCollectionsByAppUserId(uid))
                .withMessage("Benutzer mit id: " + uid +
                             " wurde nicht gefunden!");
    }

    @Test
    void removeCdCollection_shouldReturnCdCollection_whenRemoved() throws CdCollectionNotFound {
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String id= "0";
        String uid= "6";
        AppUser appUser= new AppUser(uid, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                              "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        Boolean expected= true;
        Boolean actual;

        when(mockRepo.findById(id))
                .thenReturn(Optional.of(cdCollection));
        actual= service.removeCdCollection(id);
        assertEquals(expected, actual);
    }

    @Test
    void removeCdCollection_shouldThrowException_whenCdCollectionNotFound(){
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String id= "0";

        assertThatExceptionOfType(CdCollectionNotFound.class)
                .isThrownBy( () ->
                        service.removeCdCollection(id))
                .withMessage("Sammlung mit id: " + id +
                             " wurde nicht gefunden!");
    }

    @Test
    void getCdCollectionById_shouldReturnCdCollection_whenFoundInDatabase() throws CdCollectionNotFound {
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String id= "0";
        String uid= "6";
        AppUser appUser= new AppUser(uid, "TestUser");
        CdCollection expected= new CdCollection(id,
                                          "Testsammlung",
                                                appUser,
                                                Collections.emptyList());
        CdCollection actual;

        when(mockRepo.findById(id))
                .thenReturn(Optional.of(expected));
        actual= service.getCdCollectionById(id);
        assertEquals(expected, actual);
    }

    @Test
    void getCdCollectionById_shouldThrowException_whenNotFoundInDatabase(){
        CdCollectionRepo mockRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CollectionService service= new CollectionService(mockRepo,
                                                         mockingIdService,
                                                         mockUserRepo);
        String id= "0";

        assertThatExceptionOfType(CdCollectionNotFound.class)
                .isThrownBy( () ->
                        service.getCdCollectionById(id))
                .withMessage("Sammlung mit id: " + id +
                             " wurde nicht gefunden!");
    }
}
