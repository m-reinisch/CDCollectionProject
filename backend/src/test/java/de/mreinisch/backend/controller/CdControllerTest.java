package de.mreinisch.backend.controller;

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

        userRepo.save(appUser);
        collectionRepo.save(cdCollection);
        trackRepo.save(track);
        cd.setTracks(trackList);
        repo.save(cd);
        mvc.perform(post("/api/cd")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                                {
                                    "cdTitle": "TestCD",
                                    "performer": "Tester",
                                    "publicationYear": "1971",
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
                                }
                            """))
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
}