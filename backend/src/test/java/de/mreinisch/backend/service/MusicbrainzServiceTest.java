package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.FoundCdDTO;
import de.mreinisch.backend.exception.BarcodeNotFound;
import de.mreinisch.backend.exception.InquiryNotPossible;
import de.mreinisch.backend.exception.UnexpectedSeriousError;
import de.mreinisch.backend.model.ResponseTrack;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


class MusicbrainzServiceTest {

    @Test
    void findCdByBarcode_shouldReturnFoundCdDTO_whenFoundInApi() throws BarcodeNotFound, InquiryNotPossible, UnexpectedSeriousError {
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        MusicbrainzService service=
                new MusicbrainzService(restClientBuilder);
        String barcode= "082839375023";
        String bmid= "67be9a0f-d852-36f3-8245-64e89a6759bd";
        ResponseTrack track= new ResponseTrack(1,
                                            "If You Love Somebody Set Them Free",
                                                "04:16");
        List<ResponseTrack> tracks= new ArrayList<>(List.of(track));
        FoundCdDTO expected=
                new FoundCdDTO("The Dream of the Blue Turtles",
                             "Sting", 1985,
                                "http://coverartarchive.org/release/67be9a0f-d852-36f3-8245-64e89a6759bd/3877346050.jpg",
                                tracks);
        FoundCdDTO actual;

        mockRestServiceServer.expect(
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
        mockRestServiceServer.expect(
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
        mockRestServiceServer.expect(
                    requestTo("https://coverartarchive.org/release/" +
                            bmid))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("""
            {
                "images": [{
                    "front": true,
                    "back": false,
                    "image": "http://coverartarchive.org/release/67be9a0f-d852-36f3-8245-64e89a6759bd/3877346050.jpg"
                }]
            }
            """, MediaType.APPLICATION_JSON));
        actual=service.findCdByBarcode(barcode);
        assertEquals(expected, actual);
    }

    @Test
    void findCdByBarcode_shouldThrowException_whenBarcodeNotFound(){
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        MusicbrainzService service=
                new MusicbrainzService(restClientBuilder);
        String barcode= "982839375023";

        mockRestServiceServer.expect(
                    requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                            barcode + "&limit=1&fmt=json"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));
        assertThatExceptionOfType(BarcodeNotFound.class)
                .isThrownBy( () -> service.findCdByBarcode(barcode) )
                .withMessage("Barcode: " + barcode + " nicht gefunden!");
    }

    @Test
    void findCdByBarcode_shouldReturnCdWithEmptyList_whenBMIDNotFound() throws BarcodeNotFound, InquiryNotPossible, UnexpectedSeriousError {
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        MusicbrainzService service=
                new MusicbrainzService(restClientBuilder);
        String barcode= "082839375023";
        String bmid= "07be9a0f-d852-36f3-8245-64e89a6759bd";
        FoundCdDTO expected=
                new FoundCdDTO("The Dream of the Blue Turtles",
                             "Sting", 1985,
                                "http://coverartarchive.org/release/67be9a0f-d852-36f3-8245-64e89a6759bd/3877346050.jpg",
                                Collections.emptyList());
        FoundCdDTO actual;

        mockRestServiceServer.expect(
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
        mockRestServiceServer.expect(
                    requestTo("https://musicbrainz.org/ws/2/release/" +
                            bmid + "?inc=aliases+recordings&fmt=json"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        mockRestServiceServer.expect(
                    requestTo("https://coverartarchive.org/release/" +
                            bmid))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("""
            {
                "images": [{
                    "front": true,
                    "back": false,
                    "image": "http://coverartarchive.org/release/67be9a0f-d852-36f3-8245-64e89a6759bd/3877346050.jpg"
                }]
            }
            """, MediaType.APPLICATION_JSON));
        actual=service.findCdByBarcode(barcode);
        assertEquals(expected, actual);
    }

    @Test
    void findCdByBarcode_shouldThrowException_whenServerNotAvailable() {
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        MusicbrainzService service=
                new MusicbrainzService(restClientBuilder);
        String barcode= "982839375023";

        mockRestServiceServer.expect(
                        requestTo("https://musicbrainz.org/ws/2/release/?query=barcode:" +
                                barcode + "&limit=1&fmt=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));
        assertThatExceptionOfType(InquiryNotPossible.class)
                .isThrownBy( () -> service.findCdByBarcode(barcode) )
                .withMessage("Anfrage zurzeit nicht möglich!");
    }
}
