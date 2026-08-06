package de.mreinisch.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.MockServerRestClientCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MusicbrainzControllerTest {
    @TestConfiguration()
    static class TestConfig {
        private final MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
        private final RestClient.Builder customizedBuilder = RestClient.builder();

        public TestConfig() {
            customizer.customize(customizedBuilder);
        }

        @Bean
        public RestClient.Builder restClientBuilder() {
            return customizedBuilder;
        }

        @Bean
        public MockRestServiceServer mockRestServiceServer() {
            return customizer.getServer(customizedBuilder);
        }
    }

    @Autowired
    MockMvc mvc;
    @Autowired
    MockRestServiceServer restServiceServer;
    @BeforeEach
    void setUp() {
        restServiceServer.reset();
    }

    @Test
    void getCdByBarcode_shouldReturnFoundCdDTO_whenFoundInApi() throws Exception {
        String barcode= "082839375023";
        String bmid= "67be9a0f-d852-36f3-8245-64e89a6759bd";
        restServiceServer.expect(
                        requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                                barcode + "&limit=1&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                {
                    "count": 7,
                    "releases": [{
                        "id": "67be9a0f-d852-36f3-8245-64e89a6759bd",
                        "title": "The Dream of the Blue Turtles",
                        "artist-credit": [{
                            "name": "Sting"
                        }],
                        "date": "1985"
                    }]
                }
            """, MediaType.APPLICATION_JSON));
        restServiceServer.expect(
                        requestTo("https://musicbrainz.org/ws/2/release/" +
                                bmid + "?inc=aliases+recordings&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                {
                    "media": [{
                        "tracks": [
                            {
                                "title": "If You Love Somebody Set Them Free",
                                "position": 1,
                                "length": 256293
                            }
                        ]
                    }]
                }
            """, MediaType.APPLICATION_JSON));
        mvc.perform(get("/api/musicbrainz/" + barcode))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                  {
                    "cdTitle": "The Dream of the Blue Turtles",
                    "performer": "Sting",
                    "publicationYear": 1985,
                    "tracks": [
                      {
                        "position": 1,
                        "trackTitle": "If You Love Somebody Set Them Free",
                        "time": "04:16"
                      }
                    ]
                  }
                """));
    }

    @Test
    void getCdByBarcode_shouldReturnCdDtoWithEmptyTracklist_whenTracksNotFoundInApi() throws Exception {
        String barcode= "082839375023";
        String bmid= "07be9a0f-d852-36f3-8245-64e89a6759bd";
        restServiceServer.expect(
                        requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                                barcode + "&limit=1&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                {
                    "count": 7,
                    "releases": [{
                        "id": "07be9a0f-d852-36f3-8245-64e89a6759bd",
                        "title": "The Dream of the Blue Turtles",
                        "artist-credit": [{
                            "name": "Sting"
                        }],
                        "date": "1985"
                    }]
                }
            """, MediaType.APPLICATION_JSON));
        restServiceServer.expect(
                        requestTo("https://musicbrainz.org/ws/2/release/" +
                                bmid + "?inc=aliases+recordings&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                {
                    "media": [{
                        "tracks": [ ]
                    }]
                }
            """, MediaType.APPLICATION_JSON));
        mvc.perform(get("/api/musicbrainz/" + barcode))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                  {
                    "cdTitle": "The Dream of the Blue Turtles",
                    "performer": "Sting",
                    "publicationYear": 1985,
                    "tracks": [ ]
                  }
                """));
    }

    @Test
    void getCdByBarcode_shouldThrowException_whenBarcodeNotFound() throws Exception {
        String barcode= "92839375023";
        String errorMessage= "Suche erfolglos: ";

        errorMessage+= "Barcode: " + barcode + " nicht gefunden!";
        restServiceServer.expect(
                        requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                                barcode + "&limit=1&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        mvc.perform(get("/api/musicbrainz/" + barcode))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }
}