package de.mreinisch.backend.dto;

/** DTO for processing the response to the CoverArt request.
 *
 * @param front cover?
 * @param back cover?
 * @param image of cover
 */
public record ImageDTO(
        boolean front,
        boolean back,
        String image
) {
}
