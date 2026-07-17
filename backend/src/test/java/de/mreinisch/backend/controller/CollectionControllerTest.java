package de.mreinisch.backend.controller;

import de.mreinisch.backend.dto.CdCollectionDTO;
import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.repository.AppUserRepo;
import de.mreinisch.backend.repository.CdCollectionRepo;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CollectionControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CdCollectionRepo repo;
    @Autowired
    private AppUserRepo userRepo;

    @Test
    @WithMockUser
    void createCollection_shouldReturnCdCollection_whenCalledCorrect() throws Exception {
        String id= "0";
        AppUser appUser= new AppUser(id, "TestUser");
        CdCollectionDTO cdCollectionDTO=
                new CdCollectionDTO("Testsammlung", appUser);
        CdCollection cdCollection= new CdCollection(id,
                                             "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        ObjectMapper mapper= new ObjectMapper();
        String cdCollDTO= mapper.writeValueAsString(cdCollectionDTO);

        userRepo.save(appUser);
        repo.save(cdCollection);
        mvc.perform(post("/api/collections")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(cdCollDTO))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                          {
                            name: "Testsammlung"
                          }
                """))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.cds").exists());
    }

    @Test
    @WithMockUser
    void createCollection_shouldThrowException_whenAppUserNorFound() throws Exception {
        String uid= "6";
        String id= "0";
        AppUser appUser= new AppUser(uid, "TestUser");
        AppUser failUser= new AppUser(id, "TestUser");
        CdCollectionDTO cdCollectionDTO=
                new CdCollectionDTO("Testsammlung", failUser);
        CdCollection cdCollection= new CdCollection(id,
                                             "Testsammlung",
                                                    appUser,
                                                    Collections.emptyList());
        ObjectMapper mapper= new ObjectMapper();
        String cdCollDTO= mapper.writeValueAsString(cdCollectionDTO);
        String errorMessage= "Unerwarteter Fehler: ";

        userRepo.save(appUser);
        repo.save(cdCollection);
        errorMessage+= "Benutzer mit id: " + id +
                        " wurde nicht gefunden!";
        mvc.perform(post("/api/collections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cdCollDTO))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }
}
