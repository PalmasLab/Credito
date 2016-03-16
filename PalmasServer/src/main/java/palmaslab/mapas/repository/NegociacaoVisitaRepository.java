package palmaslab.mapas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegociacaoVisitaRepository  extends CrudRepository<NegociacaoVisita,Long>{

	
	public NegociacaoVisita findById(long id);
	
	public List<NegociacaoVisita> findByIdcontrato(String idcontrato);
	public List<NegociacaoVisita> findByIdcliente(long idcliente);
	public NegociacaoVisita findByIdclienteAndIdcontrato(long idcliente,String idcontrato);
	//public NegociacaoVisita findByIdAndIdcontrato(long id,String idcontrato);
}

