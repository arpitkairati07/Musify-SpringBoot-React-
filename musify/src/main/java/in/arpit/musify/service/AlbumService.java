package in.arpit.musify.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.arpit.musify.document.Album;
import in.arpit.musify.dto.AlbumListResponse;
import in.arpit.musify.dto.AlbumRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import in.arpit.musify.repository.AlbumRepository;

import java.io.IOException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final Cloudinary cloudinary;

    // For Adding Album
    public Album addAlbum(AlbumRequest request) throws IOException {
        Map<String,Object> imageUploadResult = cloudinary.uploader().upload(request.getImageFile()
                        .getBytes(), ObjectUtils.asMap("resource_type","image"));

        Album newAlbum = Album.builder().name(request.getName()).description(request.getDescription())
                .bgColor(request.getBgColor()).imgUrl(imageUploadResult.get("secure_url").toString())
                .build();
        return albumRepository.save(newAlbum);
    }
    // For Fetching all albums
    public AlbumListResponse getAllAlbums() {
        var albums = albumRepository.findAll();
        if (albums.isEmpty()) {
            return new AlbumListResponse(false, "No album found", albums);
        }
        return new AlbumListResponse(true, "Albums fetched successfully", albums);
    }
    // For Removing the Album
    public Boolean removeAlbum(String id){
        Album existingAlbum = albumRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Album not found!"));
        albumRepository.delete(existingAlbum);
        return true;
    }
}
