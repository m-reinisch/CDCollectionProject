package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.*;
import de.mreinisch.backend.exception.BarcodeNotFound;
import de.mreinisch.backend.model.ResponseTrack;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
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
        List<ResponseTrack> tracks;

        try {
            answer= restClient.get()
                    .uri("?query=barcode:"  + barcode +
                            "&limit=1&fmt=json")
                    .retrieve()
                    .body(ReleaseAnswerDTO.class);
            System.out.println(answer);
            mbid= answer.releases().getFirst().id();
            tracks= findTracksByBMID(mbid);
            cd= FoundCdDTO.builder()
                    .cdTitle(answer.releases().getFirst().title())
                    .performer(
                            answer.releases().getFirst()
                                    .artistCredit().getFirst().name())
                    .publicationYear(Integer.parseInt(
                            answer.releases().getFirst().date()))
                    .tracks(tracks)
                    .build();
            return  cd;
        } catch (HttpClientErrorException _) {
            throw new BarcodeNotFound("Barcode: " + barcode +
                                      " nicht gefunden!");
        }
    }

    private List<ResponseTrack> findTracksByBMID(UUID bmid) {
        List<ResponseTrack> tracks= Collections.emptyList();
        BMIDAnswerDTO bmid_answer;

        try {
            bmid_answer= restClient.get()
                    .uri(bmid.toString() +
                            "?inc=aliases+recordings&fmt=json")
                    .retrieve()
                    .body(BMIDAnswerDTO.class);
            for(TrackDTO trackDTO: bmid_answer.media().getFirst().tracks()) {
                ResponseTrack track= ResponseTrack.builder()
                        .position(trackDTO.position())
                        .build();
                tracks.add(track);
            }
        } catch (HttpClientErrorException exception){
            System.out.println(exception.getMessage());
        }
        return tracks;
    }
}
