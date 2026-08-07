package de.mreinisch.backend.dto;

import de.mreinisch.backend.model.ResponseTrack;
import lombok.Builder;
import java.util.List;

/** DTO as the response for getCdByBarcode().
 *
 * @param cdTitle found
 * @param performer found
 * @param publicationYear found
 * @param tracks list of found tracks
 */
@Builder
public record FoundCdDTO(
        String cdTitle,
        String performer,
        int publicationYear,
        List<ResponseTrack> tracks
) {
}
