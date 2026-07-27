package de.mreinisch.backend.dto;

import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.model.Track;
import java.util.List;

/** Data transfer object for CD
 *
 * @param cdTitle of CD
 * @param performer of CD
 * @param publicationYear of CD
 * @param tracks list of included tracks
 * @param coverUrl url for cover picture
 * @param cdCollection who owns the CD
 */
public record CdDTO(
        String cdTitle,
        String performer,
        int publicationYear,
        List<Track> tracks,
        String coverUrl,
        CdCollection cdCollection
) {
}
