package palmaslab.mapas.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegociacaoLigarRepository extends CrudRepository<NegociacaoLigar,Long>{

	
	public NegociacaoLigar findById(long id);
	public List<NegociacaoLigar> findByIdcontrato(String idcontrato);
	public List<NegociacaoLigar> findByIdcliente(long idcliente);
	public NegociacaoLigar findByIdclienteAndIdcontrato(long idcliente,String idcontrato);
}