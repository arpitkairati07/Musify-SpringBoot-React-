package in.arpit.musify.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.arpit.musify.document.Song;
import in.arpit.musify.dto.SongListResponse;
import in.arpit.musify.dto.SongRequest;
import in.arpit.musify.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final Cloudinary cloudinary;

    // Add Song
    public Song addSong(SongRequest request) throws IOException {

        // Upload audio
        Map<String, Object> audioUploadResult = cloudinary.uploader().upload(
                request.getAudioFile().getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", "musify/songs"
                )
        );

        // Upload image
        Map<String, Object> imageUploadResult = cloudinary.uploader().upload(
                request.getImageFile().getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "musify/images"
                )
        );

        // Get duration
        Double durationSeconds = (Double) audioUploadResult.get("duration");
        String duration = formatDuration(durationSeconds);

        // Create song object
        Song newSong = Song.builder()
                .name(request.getName())
                .description(request.getDescription())
                .album(request.getAlbum())
                .image(imageUploadResult.get("secure_url").toString())
                .file(audioUploadResult.get("secure_url").toString())
                .duration(duration)
                .build();

        return songRepository.save(newSong);
    }

    // Format duration (mm:ss)
    private String formatDuration(Double durationSeconds) {

        if (durationSeconds == null) return "0:00";

        int minutes = durationSeconds.intValue() / 60;
        int seconds = durationSeconds.intValue() % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    // Get all songs
    public SongListResponse getAllSongs() {

        List<Song> songs = songRepository.findAll();

        if (songs.isEmpty()) {
            return new SongListResponse(true, "No songs available", songs);
        }

        return new SongListResponse(true, "Songs fetched successfully", songs);
    }

    // Remove song
    public void removeSong(String id) {

        Song existingSong = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        songRepository.delete(existingSong);
    }
}