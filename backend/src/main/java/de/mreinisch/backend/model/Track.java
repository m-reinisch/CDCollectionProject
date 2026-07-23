package de.mreinisch.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Tracks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Track {
    @Id
    @GeneratedValue
    private Integer id;
    private int position;
    private String titel;
    private String time;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cd_id")
    @JsonBackReference
    private CD cd;

    public Track(int position, String titel, String time, CD cd) {
        this.position = position;
        this.titel = titel;
        this.time = time;
        this.cd = cd;
    }
}
