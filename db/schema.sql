CREATE DATABASE IF NOT EXISTS conference_presentations;

USE conference_presentations;

CREATE TABLE sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    speaker_name VARCHAR(255) NOT NULL,
    file_upload_url TEXT NOT NULL
);

-- Optional performance tuning
CREATE INDEX idx_speaker_name ON sessions(speaker_name);
