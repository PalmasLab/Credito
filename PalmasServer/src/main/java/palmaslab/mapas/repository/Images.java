package palmaslab.mapas.repository;

import org.springframework.web.multipart.MultipartFile;

public class Images {
MultipartFile image;
public String id;


public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public MultipartFile getImage() {
	return image;
}

public void setImage(MultipartFile image) {
	this.image = image;
}

}
