package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdCollectionDTO;
import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.repository.AppUserRepo;
import de.mreinisch.backend.repository.CdCollectionRepo;
import org.junit.jupiter.api.Test;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionServiceTest {
    @Test
    void generateCollection_shouldReturnCdCollection_whenSaved() {
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
        ;
    }
}
