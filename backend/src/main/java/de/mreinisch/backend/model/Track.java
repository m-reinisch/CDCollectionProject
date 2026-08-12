package de.mreinisch.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
    private String trackTitle;
    @Pattern(regexp = "[0-5]?\\d:[0-5]\\d",
             message = "Die Zeit muss das Format mm:ss oder m:ss haben!")
    private String time;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cd_id")
    @JsonBackReference
    private CD cd;

    public Track(int position, String trackTitle, String time, CD cd) {
        this.position = position;
        this.trackTitle = trackTitle;
        this.time = time;
        this.cd = cd;
    }
}
