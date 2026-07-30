package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdDTO;
import de.mreinisch.backend.exception.CdCollectionNotFound;
import de.mreinisch.backend.exception.CdNotFound;
import de.mreinisch.backend.model.CD;
import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.model.Track;
import de.mreinisch.backend.repository.CdCollectionRepo;
import de.mreinisch.backend.repository.CdRepo;
import de.mreinisch.backend.repository.TrackRepo;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;

@Service
public class CdService {
    private final CdRepo repo;
    private final IdService idService;
    private final CdCollectionRepo collectionRepo;
    private final TrackRepo trackRepo;
    private static final String CD_TXT= "CD mit id ";
    private static final String NF_TXT= " wurde nicht gefunden!";

    public CdService(CdRepo repo, IdService idService, CdCollectionRepo collectionRepo, TrackRepo trackRepo) {
        this.repo = repo;
        this.idService = idService;
        this.collectionRepo = collectionRepo;
        this.trackRepo = trackRepo;
    }

    /** Creates a CD and saves it.
     *
     * @param cd to save
     * @return saved cd
     * @throws CdCollectionNotFound when id of collection not found
     */
    public CD generateCD(CdDTO cd) throws CdCollectionNotFound {
        CD newCD;
        String id= idService.generateId();
        CdCollection cdOwner= cd.cdCollection();
        List<Track> trackList= cd.tracks();

        if (collectionRepo.findById(cdOwner.getId()).isPresent()) {
            newCD = CD.builder()
                      .id(id)
                      .cdTitle(cd.cdTitle())
                      .performer(cd.performer())
                      .publicationYear(cd.publicationYear())
                      .totalTime(calcTotalTime(trackList))
                      .coverUrl(cd.coverUrl())
                      .cdCollection(cdOwner)
                      .build();
            for (Track track : trackList) {
                track.setCd(newCD);
                trackRepo.save(track);
            }
            newCD.setTracks(trackList);
            repo.save(newCD);
            return newCD;
        } else {
            throw new CdCollectionNotFound("CD Sammlung mit id " +
                                            cdOwner.getId() + NF_TXT);
        }
    }

    /** Deletes a CD from the database.
     *
     * @param id of the CD to be deleted
     * @return true, if deleted
     * @throws CdNotFound when CD not found
     */
    public Boolean removeCd(String id) throws CdNotFound {
        CD delCd= repo.findById(id)
                      .orElseThrow( () ->
                              new CdNotFound(CD_TXT + id + NF_TXT));

        delCd.getTracks().forEach(track ->
                                    trackRepo.delete(track));
        repo.deleteById(id);
        return true;
    }

    /** Searches for a CD in the database.
     *
     * @param id of the CD to be searched for
     * @return found CD
     * @throws CdNotFound when CD not found
     */
    public CD getCdById(String id) throws CdNotFound {
        return repo.findById(id)
                   .orElseThrow( () ->
                           new CdNotFound(CD_TXT + id + NF_TXT));
    }

    /** Modifies entries for the desired CD.
     *
     * @param id of the CD to be searched for
     * @param cd DTO with data to be changed
     * @return updated cd
     * @throws CdNotFound when CD not found
     */
    public CD updateCd(String id, CdDTO cd) throws CdNotFound {
        CD updatedCd= repo.findById(id)
                           .orElseThrow( () ->
                                   new CdNotFound(CD_TXT + id + NF_TXT));
        String totalTime= calcTotalTime(cd.tracks());
        List<Track> trackList= updatedCd.getTracks();

        updatedCd.setCdTitle(cd.cdTitle());
        updatedCd.setPerformer(cd.performer());
        updatedCd.setPublicationYear(cd.publicationYear());
        updatedCd.setCoverUrl(cd.coverUrl());
        updatedCd.setTotalTime(totalTime);
        for( Track track : trackList ) {
            Track updatedTrack=
                    findTrackByPosition(track.getPosition(),
                                        cd.tracks());

            if( updatedTrack != null ){
                track.setTrackTitle(updatedTrack.getTrackTitle());
                track.setTime(updatedTrack.getTime());
            }
        }
        updatedCd.setTracks(trackList);
        repo.save(updatedCd);
        return updatedCd;
    }

    /** Calculates the total duration of the CD from the durations of the individual tracks
     * <br />
     * Helper function is used only internally.
     * @param tracks of CD
     * @return total time of tracks
     */
    private String calcTotalTime(List<Track> tracks) {
        String totalTime = "0:00";

        for (Track track : tracks) {
            totalTime= addTimes(totalTime, track.getTime());
        }
        return totalTime;
    }

    /** Adds two times
     * <br />
     * Helper function is used only internally.
     * @param t1 first time
     * @param t2 second time
     * @return sum of times
     */
    private String addTimes(String t1, String t2) {
        String[] time1 = t1.split(":");
        Long minTime1 = Long.parseLong(time1[0]);
        Long secTime1 = Long.parseLong(time1[1]);
        Duration duration1 = Duration.ofMinutes(minTime1).plusSeconds(secTime1);
        String[] time2 = t2.split(":");
        Long minTime2 = Long.parseLong(time2[0]);
        Long secTime2 = Long.parseLong(time2[1]);
        Duration duration2 = Duration.ofMinutes(minTime2).plusSeconds(secTime2);
        Duration sum= duration1.plus(duration2);

        return String.format("%02d:%02d", sum.toMinutes(), (sum.toSeconds() % 60));
    }

    /** Searches for the specified position in the tracklist
     * <br />
     * Helper function is used only internally.
     * @param pos position to be searched for
     * @param tracks list of tracks to be searched
     * @return found track or null, if not found
     */
    private Track findTrackByPosition(int pos, List<Track> tracks) {
        Track foundTrack = null;

        for (Track track : tracks) {
            if (track.getPosition() == pos) {
                foundTrack = track;
                break;
            }
        }
        return foundTrack;
    }
}
