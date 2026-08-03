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

    /** Searches the MusicBrainz database for information about the CD using the barcode.
     *
     * @param barcode to search for
     * @return found cd information
     * @throws BarcodeNotFound if the barcode was not found
     */
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
            System.out.println(answer);
            mbid= answer.releases().getFirst().id();
            //@todo TrackList
            cd= FoundCdDTO.builder()
                    .cdTitle(answer.releases().getFirst().title())
                    .performer(
                            answer.releases().getFirst()
                                    .artistCredit().getFirst().name())
                    .publicationYear(Integer.parseInt(
                            answer.releases().getFirst().date()))
                    .build();
            return  cd;
        } catch (HttpClientErrorException _) {
            throw new BarcodeNotFound("Barcode: " + barcode +
                                      " nicht gefunden!");
        }
    }
}
