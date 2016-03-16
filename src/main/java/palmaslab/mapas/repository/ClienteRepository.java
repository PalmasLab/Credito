package palmaslab.mapas.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends CrudRepository<Cliente,Long>
{

//	public List<PostoSaude> findByName(String n);
	
	
//	public List<PostoSaude> findByTouchedCount(long l);
	
	public List<Cliente> findByLogginname(String name);
	
	
	
	
	
//	public List<Video> findByNameAndCategory(String name, String category);
	
	//add a video
//	public boolean addVideo(Video v);
	
	//Get the videos that have been added so far
//	public List<Video> getVideos();

	public Cliente findById(long id);
	
	//public List<String> findByNamesliked(Video v);
	
	//public List<Gift> findByDatatimeLessThan(
			// The @Param annotation tells tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "duration" variable used to
			// search for Videos
			//long maxduration);
	
	//public List<String> findById(long id).
}

