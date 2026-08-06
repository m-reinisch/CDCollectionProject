package de.mreinisch.backend.controller;

import de.mreinisch.backend.dto.FoundCdDTO;
import de.mreinisch.backend.exception.BarcodeNotFound;
import de.mreinisch.backend.exception.InquiryNotPossible;
import de.mreinisch.backend.exception.UnexpectedSeriousError;
import de.mreinisch.backend.service.MusicbrainzService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/musicbrainz")
public class MusicbrainzController {
    private final MusicbrainzService musicbrainzService;

    public MusicbrainzController(MusicbrainzService musicbrainzService) {
        this.musicbrainzService = musicbrainzService;
    }

    @GetMapping("/{barcode}")
    public FoundCdDTO getCdByBarcode(@PathVariable String barcode) throws BarcodeNotFound, InquiryNotPossible, UnexpectedSeriousError {
        return musicbrainzService.findCdByBarcode(barcode);
    }
}
