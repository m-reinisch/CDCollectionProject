package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.FoundCdDTO;
import de.mreinisch.backend.exception.BarcodeNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


class MusicbrainzServiceTest {

    @Test
    void findCdByBarcode_shouldReturnFoundCdDTO_whenFoundInApi() throws BarcodeNotFound {
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        MusicbrainzService service=
                new MusicbrainzService(restClientBuilder);
        String barcode= "082839375023";
        FoundCdDTO expected=
                new FoundCdDTO("The Dream of the Blue Turtles",
                             "Sting", 1985,
                                null);
        FoundCdDTO actual;

        mockRestServiceServer.expect(
                    requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                                barcode + "&limit=1&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                    {
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
        actual=service.findCdByBarcode(barcode);
        assertEquals(expected, actual);
    }

    @Test
    void findCdByBarcode_shouldThrowException_whenBarcodeNotFound() throws BarcodeNotFound {
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        MusicbrainzService service=
                new MusicbrainzService(restClientBuilder);
        String barcode= "82839375023";

        mockRestServiceServer.expect(
                        requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                                barcode + "&limit=1&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        assertThatExceptionOfType(BarcodeNotFound.class)
                .isThrownBy( () -> service.findCdByBarcode(barcode) )
                .withMessage("Barcode: " + barcode + " nicht gefunden!");
    }
}