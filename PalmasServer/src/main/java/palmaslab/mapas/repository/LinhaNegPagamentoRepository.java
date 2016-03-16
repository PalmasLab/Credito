package palmaslab.mapas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface LinhaNegPagamentoRepository extends CrudRepository<LinhaNegPagamento,Long>{

	
	public LinhaNegPagamento findById(long id);
	public List<LinhaNegPagamento> findByDataalertaBeforeAndChecked(String dataalerta,boolean checked);
	public List<LinhaNegPagamento> findByDataalertaAfterAndChecked(String dataalerta,boolean checked);
	public List<LinhaNegPagamento> findByDataalertaAndChecked(String dataalerta,boolean checked);
	public List<LinhaNegPagamento> findByChecked(boolean checked);
	public List<LinhaNegPagamento> findByAlertaativa(boolean alertaativa);
}
