package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdDTO;
import de.mreinisch.backend.exception.CdCollectionNotFound;
import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.model.CD;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.model.Track;
import de.mreinisch.backend.repository.CdCollectionRepo;
import de.mreinisch.backend.repository.CdRepo;
import de.mreinisch.backend.repository.TrackRepo;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CdServiceTest {

    @Test
    void generateCD_shouldReturnCD_whenSaved() throws CdCollectionNotFound {
        CdCollectionRepo mockCollectionRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        CdRepo mockRepo = mock(CdRepo.class);
        TrackRepo mockTrackRepo = mock(TrackRepo.class);
        CdService service= new CdService(mockRepo,
                                         mockingIdService,
                                         mockCollectionRepo,
                                         mockTrackRepo);
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                          "Testsammlung",
                                                appUser,
                                                Collections.emptyList());
        CD cd= new CD(id,"TestCD","Tester",
                1971, "06:54", null,
                      cdCollection, Collections.emptyList());
        Track track= new Track(1, 1, "TestSong",
                         "6:54", cd);
        List<Track> trackList= List.of(track);
        CdDTO cdDTO = new CdDTO("TestCD","Tester",
                       1971, trackList,
                            null, cdCollection);
        CD expected= new CD(cd);
        CD actual;

        expected.setTracks(trackList);
        when(mockingIdService.generateId()).thenReturn(id);
        when(mockCollectionRepo.findById(id)).thenReturn(Optional.of(cdCollection));
        when(mockTrackRepo.save(track)).thenReturn(track);
        when(mockRepo.save(expected)).thenReturn(expected);
        actual= service.generateCD(cdDTO);
        assertEquals(expected, actual);
    }

    @Test
    void generateCD_shouldThrowException_whenCDCollectionNotFound() {
        CdCollectionRepo mockCollectionRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        CdRepo mockRepo = mock(CdRepo.class);
        TrackRepo mockTrackRepo = mock(TrackRepo.class);
        CdService service= new CdService(mockRepo,
                                         mockingIdService,
                                         mockCollectionRepo,
                                         mockTrackRepo);
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                              "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        CD cd= new CD(id,"TestCD","Tester",
                1971, "06:54", null,
                      cdCollection, Collections.emptyList());
        Track track= new Track(1, 1, "TestSong",
                          "6:54", cd);
        List<Track> trackList= List.of(track);
        CdDTO cdDTO = new CdDTO("TestCD","Tester",
                    1971, trackList,
                         null, cdCollection);
        String errorMessage= "CD Sammlung mit id " + id +
                             " wurde nicht gefunden!";

        assertThatExceptionOfType(CdCollectionNotFound.class)
                .isThrownBy( () -> service.generateCD(cdDTO))
                .withMessage(errorMessage);
    }
}