package de.mreinisch.backend.dto;

import java.util.List;

/** DTO for the response from the request to coverartarchive.org
 *
 * @param images list of image
 */
public record CoverArtAnswerDTO(
        List<ImageDTO> images
) {
}
