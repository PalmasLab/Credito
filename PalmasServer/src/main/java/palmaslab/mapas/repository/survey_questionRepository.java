package palmaslab.mapas.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface survey_questionRepository extends CrudRepository<survey_question,Long>{

	public survey_question findById(long id);
}
