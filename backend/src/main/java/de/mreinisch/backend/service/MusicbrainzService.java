package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.*;
import de.mreinisch.backend.exception.BarcodeNotFound;
import de.mreinisch.backend.exception.InquiryNotPossible;
import de.mreinisch.backend.exception.UnexpectedSeriousError;
import de.mreinisch.backend.model.ResponseTrack;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.time.Duration;
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
     * @throws InquiryNotPossible if MusicBrainz doesn't respond
     */
    public FoundCdDTO findCdByBarcode(String barcode) throws BarcodeNotFound, InquiryNotPossible, UnexpectedSeriousError {
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
            if (answer.count() == 0){
                throw  new BarcodeNotFound("Barcode: " + barcode +
                                           " nicht gefunden!");
            }
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
        } catch (HttpServerErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE){
                throw new InquiryNotPossible("Anfrage zurzeit nicht möglich!");
            } else {
                throw new BarcodeNotFound("Barcode: " + barcode +
                                          " nicht gefunden!");
            }
        } catch (NullPointerException _){
            throw new UnexpectedSeriousError("CD nicht gefunden!");
        }
    }

    /** Look for the tracks belonging to the CD using the BMID.
     * <br />
     * Helper function is used only internally.
     * @param bmid determined in the first inquiry
     * @return list of found tracks or empty list
     */
    private List<ResponseTrack> findTracksByBMID(UUID bmid) throws UnexpectedSeriousError, InquiryNotPossible {
        List<ResponseTrack> tracks= new java.util.ArrayList<>(Collections.emptyList());
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
                        .trackTitle(trackDTO.title())
                        .time(convertLengthToTime(trackDTO.length()))
                        .build();
                tracks.add(track);
            }
        } catch (HttpServerErrorException exception){
            if (exception.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE){
                throw new InquiryNotPossible("Anfrage zurzeit nicht möglich!");
            }
        } catch (NullPointerException _){
            throw new UnexpectedSeriousError("Stück nicht gefunden!");
        }
        return tracks;
    }

    /** Converts a duration in milliseconds into a formatted time string.
     * <br />
     * Helper function is used only internally.
     * @param length of track in ms
     * @return time string
     */
    private String convertLengthToTime(long length) {
        Duration time = Duration.ofMillis(length);

        return  String.format("%02d:%02d", time.toMinutes(), (time.toSeconds() % 60));
    }
}
