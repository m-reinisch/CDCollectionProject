package de.mreinisch.backend.controller;

import de.mreinisch.backend.dto.CdDTO;
import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.model.CD;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.model.Track;
import de.mreinisch.backend.repository.AppUserRepo;
import de.mreinisch.backend.repository.CdCollectionRepo;
import de.mreinisch.backend.repository.CdRepo;
import de.mreinisch.backend.repository.TrackRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CdControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CdCollectionRepo collectionRepo;
    @Autowired
    private AppUserRepo userRepo;
    @Autowired
    private CdRepo repo;
    @Autowired
    private TrackRepo trackRepo;

    @Test
    @WithMockUser
    void createCd_shouldReturnCD_whenCalledCorrectly() throws Exception {
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                              "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        CD cd= new CD(id,"TestCD","Tester",
                1971, "06:54", null,
                        cdCollection, Collections.emptyList());
        Track track= new Track( 1, "TestSong",
                            "6:54", cd);
        List<Track> trackList= List.of(track);
        CdDTO cdDTO= new CdDTO("TestCD", "Tester",
                        1971, trackList, null,
                                cdCollection);
        ObjectMapper mapper= new ObjectMapper();
        String jsonCd = mapper.writeValueAsString(cdDTO);

        userRepo.save(appUser);
        collectionRepo.save(cdCollection);
        trackRepo.save(track);
        cd.setTracks(trackList);
        repo.save(cd);
        mvc.perform(post("/api/cd")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonCd))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                              {
                                "cdTitle": "TestCD",
                                "performer": "Tester",
                                "publicationYear": 1971,
                                "totalTime": "06:54"
                              },
                                "tracks": [
                                    {
                                        "position": "1",
                                        "titel": "TestSong",
                                        "time": "6:54"
                                    }
                                ],
                                "cdCollection": {
                                    "id": "0",
                                    "name": "Testsammlung"
                                }
                            """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockUser
    void createCd_shouldThrowException_whenCdCollectionNotFound() throws Exception {
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                              "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        Track track= new Track( 1, "TestSong",
                                "6:54", null);
        List<Track> trackList= List.of(track);
        CdDTO cdDTO= new CdDTO("TestCD", "Tester",
                        1971, trackList, null,
                                cdCollection);
        ObjectMapper mapper= new ObjectMapper();
        String jsonCd = mapper.writeValueAsString(cdDTO);
        String errorMessage= "Unerwarteter Fehler: ";

        userRepo.save(appUser);
        errorMessage+= "CD Sammlung mit id " + id +
                       " wurde nicht gefunden!";
        mvc.perform(post("/api/cd")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonCd))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    @WithMockUser
    void deleteCd_shouldReturnTrue_whenCdDeleted() throws Exception {
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                              "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        CD cd= new CD(id,"TestCD","Tester",
                1971, "06:54", null,
                        cdCollection, Collections.emptyList());
        Track track= new Track( 1, "TestSong",
                                "6:54", cd);
        List<Track> trackList= List.of(track);
        CD cd1= new CD(cd);

        userRepo.save(appUser);
        collectionRepo.save(cdCollection);
        trackRepo.save(track);
        cd1.setTracks(trackList);
        repo.save(cd1);
        mvc.perform(delete("/api/cd/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

//    @Test
//    @WithMockUser
//    void deleteCd_shouldThrowException_whenCdNotFound() throws Exception {
//        String id= "0";
//        AppUser appUser= new AppUser(id, "TestUser");
//        CdCollection cdCollection= new CdCollection(id,
//                "Testsammlung",
//                appUser,
//                Collections.emptyList());
//        Track track= new Track( 1, "TestSong",
//                "6:54", null);
//        List<Track> trackList= List.of(track);
//        CdDTO cdDTO= new CdDTO("TestCD", "Tester",
//                1971, trackList, null,
//                cdCollection);
//        ObjectMapper mapper= new ObjectMapper();
//        String jsonCd = mapper.writeValueAsString(cdDTO);
//        String errorMessage= "Unerwarteter Fehler: ";
//
//        userRepo.save(appUser);
//        errorMessage+= "CD Sammlung mit id " + id +
//                " wurde nicht gefunden!";
//        mvc.perform(post("/api/cd")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonCd))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string(errorMessage));
//    }
}