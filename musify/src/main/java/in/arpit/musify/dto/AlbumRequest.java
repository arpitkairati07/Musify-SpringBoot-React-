package in.arpit.musify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumRequest {
    private String id;
    private String name;
    private String description;
    private String bgColor;
    private MultipartFile imageFile;
}
