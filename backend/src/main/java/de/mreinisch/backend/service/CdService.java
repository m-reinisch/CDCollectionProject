package de.mreinisch.backend.service;

import de.mreinisch.backend.dto.CdDTO;
import de.mreinisch.backend.exception.CdCollectionNotFound;
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

    public CdService(CdRepo repo, IdService idService, CdCollectionRepo collectionRepo, TrackRepo trackRepo) {
        this.repo = repo;
        this.idService = idService;
        this.collectionRepo = collectionRepo;
        this.trackRepo = trackRepo;
    }

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
                                            cdOwner.getId() +
                                            " wurde nicht gefunden!");
        }
    }

    /** Calculates the total duration of the CD from the durations of the individual tracks.
     *
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
     *
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
}
