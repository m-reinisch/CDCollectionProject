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
                1971, "06:54",
                "https://test.de/img.jpg",
                        cdCollection, Collections.emptyList());
        Track track= new Track( 1, "TestSong",
                            "6:54", cd);
        List<Track> trackList= List.of(track);
        CdDTO cdDTO= new CdDTO("TestCD", "Tester",
                        1971, trackList,
                        "https://test.de/img.jpg",
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
                                "totalTime": "06:54",
                                "coverUrl": "https://test.de/img.jpg"
                              }
                            """))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tracks").isNotEmpty());
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
    void createCd_shouldThrowException_whenCalledNotCorrectly() throws Exception {
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollection cdCollection= new CdCollection(id,
                                              "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        CD cd= new CD(id,"TestCD","Tester",
                1971, "06:54",
                "https://test.de/img.jpg",
                    cdCollection, Collections.emptyList());
        Track track= new Track( 1, "TestSong",
                                "6:54", cd);
        List<Track> trackList= List.of(track);
        CdDTO cdDTO= new CdDTO(" ", "Tester",
                        1971, trackList,
                        "https://test.de/img.jpg",
                                cdCollection);
        ObjectMapper mapper= new ObjectMapper();
        String jsonCd = mapper.writeValueAsString(cdDTO);
        String errorJson= """
                            {
                                "cdTitle": "Der Titel ist erforderlich!"
                            }
                          """;

        userRepo.save(appUser);
        collectionRepo.save(cdCollection);
        trackRepo.save(track);
        cd.setTracks(trackList);
        repo.save(cd);
        mvc.perform(post("/api/cd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCd))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    @WithMockUser
    void readCdById_shouldReturnCD_whenCdFoundInDatabase() throws Exception {
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
        ObjectMapper mapper= new ObjectMapper();

        userRepo.save(appUser);
        collectionRepo.save(cdCollection);
        trackRepo.save(track);
        cd.setTracks(trackList);
        repo.save(cd);
        String jsonCd = mapper.writeValueAsString(cd);
        mvc.perform(get("/api/cd/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonCd));
    }

    @Test
    @WithMockUser
    void readCdById_shouldThrowException_whenCdNotFound() throws Exception {
        String id= "0";
        String errorMessage= "Unerwarteter Fehler: ";

        errorMessage+= "CD mit id " + id + " wurde nicht gefunden!";
        mvc.perform(get("/api/cd/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    @WithMockUser
    void updateCDById_shouldReturnCD_whenCdUpdated() throws Exception {
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
        ObjectMapper mapper= new ObjectMapper();

        userRepo.save(appUser);
        collectionRepo.save(cdCollection);
        trackRepo.save(track);
        CdDTO cdDTO= new CdDTO("TestCD","Max",
                        1971, trackList, null,
                                cdCollection);
        String jsonCdDTO = mapper.writeValueAsString(cdDTO);
        cd.setTracks(trackList);
        cd.setPerformer("Max");
        repo.save(cd);
        String jsonCd = mapper.writeValueAsString(cd);
        mvc.perform(put("/api/cd/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCdDTO))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonCd));
    }

    @Test
    @WithMockUser
    void updateCDById_shouldThrowException_whenCdNotFound() throws Exception {
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
        CdDTO cdDTO= new CdDTO("TestCD","Max",
                        1971, trackList, null,
                                cdCollection);
        ObjectMapper mapper= new ObjectMapper();
        String jsonCdDTO = mapper.writeValueAsString(cdDTO);
        String fid= "6";
        String errorMessage= "Unerwarteter Fehler: ";

        errorMessage+= "CD mit id " + fid + " wurde nicht gefunden!";
        mvc.perform(put("/api/cd/" + fid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCdDTO))
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

        userRepo.save(appUser);
        collectionRepo.save(cdCollection);
        trackRepo.save(track);
        cd.setTracks(trackList);
        repo.save(cd);
        mvc.perform(delete("/api/cd/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    void deleteCd_shouldThrowException_whenCdNotFound() throws Exception {
        String id= "0";
        String errorMessage= "Unerwarteter Fehler: ";

        errorMessage+= "CD mit id " + id + " wurde nicht gefunden!";
        mvc.perform(delete("/api/cd/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }
}