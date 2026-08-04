package de.mreinisch.backend.dto;

import java.util.List;

/** DTO for evaluating the response to the inquiry involving BMID.
 *
 * @param tracks list of track
 */
public record MediaDTO(
        List<TrackDTO> tracks
) {
}
