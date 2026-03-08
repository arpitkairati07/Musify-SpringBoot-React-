package in.arpit.musify.controller;

import in.arpit.musify.document.Album;
import in.arpit.musify.dto.AlbumRequest;
import in.arpit.musify.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;
@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    // For Creating Album
    @PostMapping
    public ResponseEntity<?> addAlbum(@RequestPart("request") String request,
                                      @RequestPart("file") MultipartFile file) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            AlbumRequest albumRequest = objectMapper.readValue(request, AlbumRequest.class);
            albumRequest.setImageFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(albumService.addAlbum(albumRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //For Fetching Albums
    @GetMapping
    public ResponseEntity<?> listAlbums(){
        try{
            return ResponseEntity.ok(albumService.getAllAlbums());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // For Deleting Album
    @DeleteMapping("/{id}")
   public ResponseEntity<?> removeAlbum(@PathVariable String id){
        try{
            boolean removed = albumService.removeAlbum(id);
            if(removed){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }else{
                return ResponseEntity.badRequest().build();
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
   }
}
