package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdDTO;
import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.model.CD;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.model.Track;
import de.mreinisch.backend.repository.AppUserRepo;
import de.mreinisch.backend.repository.CdCollectionRepo;
import de.mreinisch.backend.repository.CdRepo;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CdServiceTest {

    @Test
    void generateCD_shouldReturnCD_whenSaved() {
        CdCollectionRepo mockCollectionRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        AppUserRepo mockUserRepo= mock(AppUserRepo.class);
        CdRepo mockRepo = mock(CdRepo.class);
        CdService service= new CdService(mockRepo, mockingIdService,
                                         mockCollectionRepo);
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                          "Testsammlung",
                                                appUser,
                                                Collections.emptyList());
        CD cd= new CD(id,"TestCD","Tester",
                1971, "06:54", null,
                      cdCollection, Collections.emptyList());
        Track track= new Track(id, 1, "TestSong",
                         "6:54", cd);
        List<Track> trackList= List.of(track);
        CdDTO cdDTO = new CdDTO("TestCD","Tester",
                       1971, trackList,
                            null, cdCollection);
        CD expected= new CD(cd);
        CD actual;

        expected.setTracks(trackList);
        when(mockingIdService.generateId()).thenReturn(id);
        when(mockUserRepo.findAppUserById(id)).thenReturn(appUser);
        when(mockRepo.save(expected)).thenReturn(expected);
        actual= service.generateCD(cdDTO);
        assertEquals(expected, actual);
    }
}