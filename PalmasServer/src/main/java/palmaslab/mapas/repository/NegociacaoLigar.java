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
public class NegociacaoLigar {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	String periodicidade_ligar;
	String datainicio_ligar;
	String idcontrato;
	long idcliente;
	String numero_repeticoes_ligar;
	
	@ElementCollection(fetch=FetchType.EAGER)
	List<LinhaNegLigar> tabela_ligar = new ArrayList<LinhaNegLigar>();
	
	public NegociacaoLigar()
	{
		
	}
	


	public String getNumero_repeticoes_ligar() {
		return numero_repeticoes_ligar;
	}



	public void setNumero_repeticoes_ligar(String numero_repeticoes_ligar) {
		this.numero_repeticoes_ligar = numero_repeticoes_ligar;
	}



	public void addLinhaNegLigar(LinhaNegLigar lnp)
	{
		tabela_ligar.add(lnp);
	}
	
	public void deleteLinhaNegLigar(LinhaNegLigar lnp)
	{
		tabela_ligar.remove(lnp);
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getPeriodicidade_ligar() {
		return periodicidade_ligar;
	}
	public void setPeriodicidade_ligar(String periodicidade_ligar) {
		this.periodicidade_ligar = periodicidade_ligar;
	}
	public String getDatainicio_ligar() {
		return datainicio_ligar;
	}
	public void setDatainicio_ligar(String datainicio_ligar) {
		this.datainicio_ligar = datainicio_ligar;
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
	public List<LinhaNegLigar> getTabela_ligar() {
		return tabela_ligar;
	}
	public void setTabela_ligar(List<LinhaNegLigar> tabela_ligar) {
		this.tabela_ligar = tabela_ligar;
	}
	
}
