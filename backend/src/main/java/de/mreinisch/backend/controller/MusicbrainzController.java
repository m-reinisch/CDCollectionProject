package de.mreinisch.backend.controller;

import de.mreinisch.backend.dto.FoundCdDTO;
import de.mreinisch.backend.exception.BarcodeNotFound;
import de.mreinisch.backend.exception.InquiryNotPossible;
import de.mreinisch.backend.exception.UnexpectedSeriousError;
import de.mreinisch.backend.service.MusicbrainzService;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/musicbrainz")
@Validated
public class MusicbrainzController {
    private final MusicbrainzService musicbrainzService;

    public MusicbrainzController(MusicbrainzService musicbrainzService) {
        this.musicbrainzService = musicbrainzService;
    }

    @GetMapping("/{barcode}")
    public FoundCdDTO getCdByBarcode(@PathVariable
                                     @Pattern(regexp = "\\d{12,14}",
                                              message = "Der Barcode darf nur Zahlen enthalten und muss zwischen 12 und 14 Zeichen lang sein!")
                                     String barcode)
                                     throws BarcodeNotFound,
                                            InquiryNotPossible,
                                            UnexpectedSeriousError {
        return musicbrainzService.findCdByBarcode(barcode);
    }
}
