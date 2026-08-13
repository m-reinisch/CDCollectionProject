package de.mreinisch.backend.dto;

import de.mreinisch.backend.model.CdCollection;
import de.mreinisch.backend.model.Genres;
import de.mreinisch.backend.model.Track;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/** Data transfer object for CD
 *
 * @param cdTitle of CD
 * @param performer of CD
 * @param publicationYear of CD
 * @param genres of CD
 * @param storageLocation of CD
 * @param tracks list of included tracks
 * @param coverUrl url for cover picture
 * @param cdCollection who owns the CD
 */
public record CdDTO(
        @NotBlank(message = "Der Titel ist erforderlich!")
        String cdTitle,
        @NotBlank(message = "Der Interpret ist erforderlich!")
        String performer,
        int publicationYear,
        Genres genres,
        String storageLocation,
        @Valid
        List<Track> tracks,
        String coverUrl,
        @NotNull(message = "Zugehörige CdCollection ist erforderlich!")
        CdCollection cdCollection
) {
}
