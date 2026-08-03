package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.FoundCdDTO;
import de.mreinisch.backend.dto.ReleaseAnswerDTO;
import de.mreinisch.backend.exception.BarcodeNotFound;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.UUID;

@Service
public class MusicbrainzService {
    private final RestClient restClient;

    public MusicbrainzService(RestClient.Builder builder) {
        this.restClient= builder
                .baseUrl("https://musicbrainz.org/ws/2/release/")
                .build();
    }

    public FoundCdDTO findCdByBarcode(String barcode) throws BarcodeNotFound {
        ReleaseAnswerDTO answer;
        FoundCdDTO cd;
        UUID mbid;

        try {
            answer= restClient.get()
                    .uri("?query=barcode:"  + barcode +
                            "&limit=1&fmt=json")
                    .retrieve()
                    .body(ReleaseAnswerDTO.class);
            mbid= answer.releases().getFirst().id();
            cd= FoundCdDTO.builder()
                    .cdTitle(answer.releases().getFirst().title())
                    .build();
            return  cd;
        } catch (HttpClientErrorException _) {
            throw new BarcodeNotFound("Barcode: " + barcode +
                                      " nicht gefunden!");
        }
    }
}
