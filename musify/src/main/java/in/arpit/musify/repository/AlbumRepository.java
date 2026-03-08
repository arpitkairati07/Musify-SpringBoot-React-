package in.arpit.musify.repository;

import in.arpit.musify.document.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album,String> {

}
