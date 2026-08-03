package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.FoundCdDTO;
import de.mreinisch.backend.exception.BarcodeNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
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
                                Collections.emptyList());
        ObjectMapper mapper= new ObjectMapper();
        String jsonBody= mapper.writeValueAsString(expected);
        FoundCdDTO actual;

        mockRestServiceServer.expect(
                    requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                                barcode + "&limit=1&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonBody,
                        MediaType.APPLICATION_JSON));
        actual=service.findCdByBarcode(barcode);
        assertEquals(expected, actual);
    }
}