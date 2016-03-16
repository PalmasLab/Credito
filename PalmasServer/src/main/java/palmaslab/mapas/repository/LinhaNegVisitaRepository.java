package palmaslab.mapas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinhaNegVisitaRepository extends CrudRepository<LinhaNegVisita,Long>{

	
	public LinhaNegVisita findById(long id);
	public List<LinhaNegVisita> findByDataalertaBeforeAndChecked(Date dataalerta,boolean checked);
	public List<LinhaNegVisita> findByChecked(boolean checked);
	public List<LinhaNegVisita> findByAlertaativa(boolean alertaativa);
}
