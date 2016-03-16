package palmaslab.mapas.repository;

import org.springframework.web.multipart.MultipartFile;

public class Mapaexcel {
	MultipartFile excel;
	public String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public MultipartFile getExcel() {
		return excel;
	}

	public void setImage(MultipartFile excel) {
		this.excel = excel;
	}
	
}
