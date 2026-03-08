package in.arpit.musify.dto;

import in.arpit.musify.document.Album;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AlbumListResponse {

    private boolean success;
    private String message;
    private List<Album> albums;

}