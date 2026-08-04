package de.mreinisch.backend.dto;

import java.util.List;

/** DTO for the response to the inquiry with BMID.
 *
 * @param media list of media
 */
public record BMIDAnswerDTO(
        List<MediaDTO> media
) {
}
