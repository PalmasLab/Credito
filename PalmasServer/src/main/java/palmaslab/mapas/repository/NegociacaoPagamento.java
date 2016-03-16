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
public class NegociacaoPagamento {

	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	String valorinicial;
	String numeroparcelas;
	String valor_por_parcela;
	String periodicidade;
	
	String datainicio;
	String idcontrato;
	long idcliente;
	@ElementCollection(fetch=FetchType.EAGER)
	List<LinhaNegPagamento> tabela_pagamento = new ArrayList<LinhaNegPagamento>();

	
	public NegociacaoPagamento()
	{
		
	}
	
	public void addLinhaNegPagamento(LinhaNegPagamento lnp)
	{
		tabela_pagamento.add(lnp);
	}
	
	public void deleteLinhaNegPagamento(LinhaNegPagamento lnp)
	{
		tabela_pagamento.remove(lnp);
	}

	public String getDatainicio() {
		return datainicio;
	}




	public void setId(long id) {
		this.id = id;
	}




	public void setDatainicio(String datainicio) {
		this.datainicio = datainicio;
	}




	public String getValorinicial() {
		return valorinicial;
	}




	public void setValorinicial(String valorinicial) {
		this.valorinicial = valorinicial;
	}




	
	public String getNumeroparcelas() {
		return numeroparcelas;
	}




	public void setNumeroparcelas(String numeroparcelas) {
		this.numeroparcelas = numeroparcelas;
	}




	public long getIdcliente() {
		return idcliente;
	}
	public void setIdcliente(long idcliente) {
		this.idcliente = idcliente;
	}
	

	public String getValor_por_parcela() {
		return valor_por_parcela;
	}

	public void setValor_por_parcela(String valor_por_parcela) {
		this.valor_por_parcela = valor_por_parcela;
	}





	public String getPeriodicidade() {
		return periodicidade;
	}




	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}




	public String getIdcontrato() {
		return idcontrato;
	}

	public void setIdcontrato(String idcontrato) {
		this.idcontrato = idcontrato;
	}

	public List<LinhaNegPagamento> getTabela_pagamento() {
		return tabela_pagamento;
	}

	public void setTabela_pagamento(List<LinhaNegPagamento> tabela_pagamento) {
		this.tabela_pagamento = tabela_pagamento;
	}

	public long getId() {
		return id;
	}
	
	
	
	
}
