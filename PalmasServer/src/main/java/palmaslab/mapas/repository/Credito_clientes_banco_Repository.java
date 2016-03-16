package palmaslab.mapas.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface Credito_clientes_banco_Repository extends CrudRepository<Credito_clientes_banco,Long>{
	
	public Credito_clientes_banco findById(long id);
	public Credito_clientes_banco findByCodigocliente(long codigo_cliente);
	public List<Credito_clientes_banco> findByEndereco(String endereco);
	public List<Credito_clientes_banco> findByEnderecoContaining(String endereco);
	public List<Credito_clientes_banco> findByGpspositionupdated(boolean Gpspositionupdated);
}
