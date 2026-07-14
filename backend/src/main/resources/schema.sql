CREATE TABLE Users(
    id VARCHAR(65535) PRIMARY KEY,
    username VARCHAR(100) NOT NULL
);

CREATE TABLE Collections(
    id VARCHAR(65535) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    owner VARCHAR(65535) NOT NULL,

    CONSTRAINT fk_collections_users
        FOREIGN KEY(owner) REFERENCES Users(id)
);

CREATE TABLE CDs(
    id VARCHAR(65535) PRIMARY KEY,
    titel VARCHAR(100) NOT NULL,
    performer VARCHAR(65535),
    year YEAR,
    total_time TIME,
    cover_url VARCHAR(65535),
    associated_collection VARCHAR(65535) NOT NULL,

    CONSTRAINT fk_cds_collections
        FOREIGN KEY(associated_collection) REFERENCES Collections(id)
);

CREATE TABLE Tracks(
    id VARCHAR(65535) PRIMARY KEY,
    titel VARCHAR(100) NOT NULL,
    time TIME,
    associated_cd VARCHAR(65535) NOT NULL,

    CONSTRAINT fk_tracks_cds
        FOREIGN KEY(associated_cd) REFERENCES CDs(id)
);
