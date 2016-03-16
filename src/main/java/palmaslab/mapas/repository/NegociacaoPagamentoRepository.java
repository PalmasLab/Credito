package palmaslab.mapas.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface NegociacaoPagamentoRepository  extends CrudRepository<NegociacaoPagamento,Long>{

	
	public NegociacaoPagamento findById(long id);
	
	public List<NegociacaoPagamento> findByIdcontrato(String idcontrato);
	public List<NegociacaoPagamento> findByIdcliente(long idcliente);
	public NegociacaoPagamento findByIdclienteAndIdcontrato(long idcliente,String idcontrato);
}
