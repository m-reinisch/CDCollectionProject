package de.mreinisch.backend.dto;

import de.mreinisch.backend.model.Track;
import lombok.Builder;

import java.util.List;

/**
 *
 * @param cdTitle
 * @param performer
 * @param publicationYear
 * @param tracks
 */
@Builder
public record FoundCdDTO(
        String cdTitle,
        String performer,
        int publicationYear,
        List<Track> tracks
) {
}
