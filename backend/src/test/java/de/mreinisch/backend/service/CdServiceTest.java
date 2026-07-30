package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdDTO;
import de.mreinisch.backend.exception.CdCollectionNotFound;
import de.mreinisch.backend.exception.CdNotFound;
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
import static org.mockito.Mockito.*;

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
        verify(mockRepo, times(1)).save(expected);
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

        cd.setTracks(trackList);
        assertThatExceptionOfType(CdCollectionNotFound.class)
                .isThrownBy( () -> service.generateCD(cdDTO))
                .withMessage(errorMessage);
        verify(mockRepo, times(0)).save(cd);
    }

    @Test
    void deleteCd_shouldReturnTrue_whenCdDeleted() throws CdNotFound {
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
        Boolean expected= true;
        Boolean actual;

        cd.setTracks(trackList);
        when(mockRepo.findById(id)).thenReturn(Optional.of(cd));
        actual= service.removeCd(id);
        assertEquals(expected, actual);
        verify(mockRepo, times(1)).findById(id);
        verify(mockRepo, times(1)).deleteById(id);
    }

    @Test
    void deleteCd_shouldThrowException_whenCdNotFound(){
        CdCollectionRepo mockCollectionRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        CdRepo mockRepo = mock(CdRepo.class);
        TrackRepo mockTrackRepo = mock(TrackRepo.class);
        CdService service= new CdService(mockRepo,
                mockingIdService,
                mockCollectionRepo,
                mockTrackRepo);
        String id= "0";
        String errorMessage= "CD mit id " + id +
                             " wurde nicht gefunden!";

        assertThatExceptionOfType(CdNotFound.class)
                .isThrownBy( () -> service.removeCd(id) )
                .withMessage(errorMessage);
        verify(mockRepo, times(1)).findById(id);
        verify(mockRepo, times(0)).deleteById(id);
    }

    @Test
    void getCdById_shouldReturnCD_whenCdInDatabase() throws CdNotFound {
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
        CD expected= new CD(cd);
        CD actual;

        expected.setTracks(trackList);
        when(mockRepo.findById(id)).thenReturn(Optional.of(expected));
        actual= service.getCdById(id);
        assertEquals(expected, actual);
        verify(mockRepo, times(1)).findById(id);
    }

    @Test
    void getCdById_shouldThrowException_whenCdNotFound(){
        CdCollectionRepo mockCollectionRepo= mock(CdCollectionRepo.class);
        IdService mockingIdService= mock(IdService.class);
        CdRepo mockRepo = mock(CdRepo.class);
        TrackRepo mockTrackRepo = mock(TrackRepo.class);
        CdService service= new CdService(mockRepo,
                                         mockingIdService,
                                         mockCollectionRepo,
                                         mockTrackRepo);
        String id= "0";
        String errorMessage= "CD mit id " + id +
                             " wurde nicht gefunden!";

        assertThatExceptionOfType(CdNotFound.class)
                .isThrownBy( () -> service.getCdById(id) )
                .withMessage(errorMessage);
        verify(mockRepo, times(1)).findById(id);
    }

    @Test
    void updateCd_shouldReturnCD_whenCdInDatabase() throws CdNotFound {
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
        CdDTO cdDTO= new CdDTO("TestCD","Max",
                        1971, trackList, null,
                                cdCollection);
        CD expected= new CD(cd);
        CD actual;

        expected.setTracks(trackList);
        when(mockRepo.findById(id)).thenReturn(Optional.of(expected));
        expected.setPerformer("Max");
        actual= service.updateCd(id, cdDTO);
        assertEquals(expected, actual);
        verify(mockRepo, times(1)).findById(id);
        verify(mockRepo, times(1)).save(expected);
    }

    @Test
    void updateCd_shouldThrowException_whenCdNotFound(){
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
        CdDTO cdDTO= new CdDTO("TestCD","Max",
                        1971, trackList, null,
                                cdCollection);
        String fid= "6";
        String errorMessage= "CD mit id " + fid +
                             " wurde nicht gefunden!";

        assertThatExceptionOfType(CdNotFound.class)
                .isThrownBy( () -> service.updateCd(fid, cdDTO) )
                .withMessage(errorMessage);
        verify(mockRepo, times(1)).findById(fid);
        verify(mockRepo, times(0)).save(cd);
    }
}
