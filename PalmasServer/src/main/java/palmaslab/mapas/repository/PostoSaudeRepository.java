package palmaslab.mapas.repository;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface PostoSaudeRepository extends CrudRepository<Postosaude,Long>
{

//	public List<PostoSaude> findByName(String n);
	
	
//	public List<PostoSaude> findByTouchedCount(long l);
	
	
	
	
	
	
//	public List<Video> findByNameAndCategory(String name, String category);
	
	//add a video
//	public boolean addVideo(Video v);
	
	//Get the videos that have been added so far
//	public List<Video> getVideos();

	public Postosaude findById(long id);
	
	//public List<String> findByNamesliked(Video v);
	
	//public List<Gift> findByDatatimeLessThan(
			// The @Param annotation tells tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "duration" variable used to
			// search for Videos
			//long maxduration);
	
	//public List<String> findById(long id).
}


