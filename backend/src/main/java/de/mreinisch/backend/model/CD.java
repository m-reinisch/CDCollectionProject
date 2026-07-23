package de.mreinisch.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "CDs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CD {
    @Id
    private String id;
    private String cdTitle;
    private String performer;
    private int publicationYear;
    private String totalTime;
    private String coverUrl;
    @ManyToOne
    @JoinColumn(name = "collection_id")
    @JsonBackReference
    private CdCollection cdCollection;
    @OneToMany(mappedBy = "cd", cascade = CascadeType.REFRESH, orphanRemoval = true)
    @JsonManagedReference
    private List<Track> tracks;

    public CD(CD cd) {
        id = cd.getId();
        cdTitle = cd.getCdTitle();
        performer = cd.getPerformer();
        publicationYear = cd.getPublicationYear();
        totalTime = cd.getTotalTime();
        coverUrl = cd.getCoverUrl();
        cdCollection = cd.getCdCollection();
        tracks = cd.getTracks();
    }
}
