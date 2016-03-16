package palmaslab.mapas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinhaNegLigarRepository extends CrudRepository<LinhaNegLigar,Long>{

	
	public LinhaNegLigar findById(long id);
	public List<LinhaNegLigar> findByDataalertaBeforeAndChecked(Date dataalerta,boolean checked);
	public List<LinhaNegLigar> findByChecked(boolean checked);
	public List<LinhaNegLigar> findByAlertaativa(boolean alertaativa);
	public List<LinhaNegLigar> findByIdnegociacao(long id);
}
