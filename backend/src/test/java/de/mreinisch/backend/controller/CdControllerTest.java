package de.mreinisch.backend.controller;

import de.mreinisch.backend.repository.CdCollectionRepo;
import de.mreinisch.backend.repository.CdRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CdControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CdCollectionRepo cdCollectionRepo;
    @Autowired
    private CdRepo repo;

    @Test
    void createCd() {
    }
}