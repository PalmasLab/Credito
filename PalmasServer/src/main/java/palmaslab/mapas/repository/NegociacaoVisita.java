package palmaslab.mapas.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NegociacaoVisita {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	String periodicidade_visita;
	String datainicio_visita;
	String idcontrato;
	long idcliente;
	String numero_repeticoes_visita;
	@ElementCollection(fetch=FetchType.EAGER)
	List<LinhaNegVisita> tabela_visita = new ArrayList<LinhaNegVisita>();
	
	public NegociacaoVisita()
	{
	}

	public String getNumero_repeticoes_visita() {
		return numero_repeticoes_visita;
	}

	public void setNumero_repeticoes_visita(String numero_repeticoes_visita) {
		this.numero_repeticoes_visita = numero_repeticoes_visita;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPeriodicidade_visita() {
		return periodicidade_visita;
	}

	public void setPeriodicidade_visita(String periodicidade_visita) {
		this.periodicidade_visita = periodicidade_visita;
	}

	public String getDatainicio_visita() {
		return datainicio_visita;
	}

	public void setDatainicio_visita(String datainicio_visita) {
		this.datainicio_visita = datainicio_visita;
	}

	public String getIdcontrato() {
		return idcontrato;
	}

	public void setIdcontrato(String idcontrato) {
		this.idcontrato = idcontrato;
	}

	public long getIdcliente() {
		return idcliente;
	}

	public void setIdcliente(long idcliente) {
		this.idcliente = idcliente;
	}

	public List<LinhaNegVisita> getTabela_visita() {
		return tabela_visita;
	}

	public void setTabela_visita(List<LinhaNegVisita> tabela_visita) {
		this.tabela_visita = tabela_visita;
	}
	public void addLinhaNegVisita(LinhaNegVisita lnp)
	{
		tabela_visita.add(lnp);
	}
	
	public void deleteLinhaNegVisita(LinhaNegVisita lnp)
	{
		tabela_visita.remove(lnp);
	}
	
}
