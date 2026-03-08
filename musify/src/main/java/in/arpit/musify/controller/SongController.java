package in.arpit.musify.controller;

import in.arpit.musify.document.Song;
import in.arpit.musify.dto.SongListResponse;
import in.arpit.musify.dto.SongRequest;
import in.arpit.musify.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping
    public ResponseEntity<?> addSong(@RequestPart("request") String requestString,
                                     @RequestPart("audio") MultipartFile audioFile,
                                     @RequestPart("image") MultipartFile imageFile) {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            SongRequest songRequest = objectMapper.readValue(requestString, SongRequest.class);

            songRequest.setImageFile(imageFile);
            songRequest.setAudioFile(audioFile);

            Song savedSong = songService.addSong(songRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedSong);

        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listSongs(){
        try{
            return ResponseEntity.ok(songService.getAllSongs());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SongListResponse(false,"Error fetching songs",null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeSong(@PathVariable String id){
        try{
            songService.removeSong(id);
            return ResponseEntity.ok("Song deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}