package de.mreinisch.backend.dto;

/** DTO for including the track in the API response.
 *
 * @param position on cd
 * @param title of song
 * @param length of song
 */
public record TrackDTO(
        int position,
        String title,
        long length
) {
}
