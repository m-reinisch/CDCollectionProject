package de.mreinisch.backend.model;

public enum Genres {
    AFROBEAT("Afrobeat"),
    AMBIENT("Ambient"),
    AVANTGARDE("Avantgarde"),
    BALLAD("Ballade"),
    BALLET("Ballett"),
    BAROQUE("Barock"),
    BEAT("Beat"),
    BEBOP("Bebop"),
    BIGBAND("Bigband"),
    BLUEGRASS("Bluegrass"),
    BLUES("Blues"),
    BRAZIL("Brazil"),
    BRITPOP("Britpop"),
    CANTATA("Kantate"),
    CELTIC("Celtic"),
    CHRISTMAS_MUSIC("Weihnachtsmusik"),
    CHURCH_MUSIC("Kirchenmusik"),
    CLASSICAL("Klassik"),
    CONTEMPORARY_JAZZ("Contemporary Jazz"),
    COOL_JAZZ("Cool Jazz"),
    COUNTRY("Country"),
    DANCE("Dance"),
    DANCE_MUSIC("Tanzmusik"),
    DISCO("Disco"),
    DIXIELAND("Dixieland"),
    FLAMENCO("Flamenco"),
    FOLK("Folk"),
    FREE_JAZZ("Free Jazz"),
    FUNK("Funk"),
    FUSION("Fusion"),
    GOSPEL("Gospel"),
    GOTHIC("Gothic"),
    HARD_ROCK("Hard Rock"),
    HEAVY_METAL("Heavy Metal"),
    HIP_HOP("Hip Hop"),
    HOUSE("House"),
    IRISH_FOLK("Irish Folk"),
    JAZZ("Jazz"),
    JAZZ_ROCK("Jazz Rock"),
    K_POP("K-Pop"),
    KLEZMER("Klezmer"),
    KRAUT_ROCK("Kraut Rock"),
    LATIN("Latin"),
    LATIN_JAZZ("Latin Jazz"),
    NEW_AGE("New Age"),
    NEW_GERMAN_WAVE("Neue deutsche Welle"),
    OPERA("Oper"),
    POP("Pop"),
    PUNK("Punk"),
    R_AND_B("R&B"),
    REGGAE("Reggae"),
    ROCK_AND_ROLL("Rock & Roll"),
    SALSA("Salsa"),
    SCHLAGER("Schlager"),
    SKA("Ska"),
    SMOOTH_JAZZ("Smooth Jazz"),
    SOUL("Soul"),
    SWING("Swing"),
    TANGO("Tango"),
    TECHNO("Techno"),
    TRANCE("Trance"),
    WALTZ("Walzer"),
    VOLKSTUEMLICHE_MUSIK("Volkstümliche Musik");

    private final String style;

    Genres(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
