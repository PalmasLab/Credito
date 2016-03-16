package palmaslab.mapas.repository;

public class RelatorioPagamento {

	String data;
	String nomecliente;
	String codigocliente;
	String codigocontrato;
	String situacao;
	String valorpago;
	String endereco;
	public RelatorioPagamento(String data, String nomecliente,
			String codigocliente, String codigocontrato, String situacao,
			String valorpago,String endereco) {
		super();
		this.data = data;
		this.nomecliente = nomecliente;
		this.codigocliente = codigocliente;
		this.codigocontrato = codigocontrato;
		this.situacao = situacao;
		this.valorpago = valorpago;
		this.endereco = endereco;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getNomecliente() {
		return nomecliente;
	}
	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}
	public String getCodigocliente() {
		return codigocliente;
	}
	public void setCodigocliente(String codigocliente) {
		this.codigocliente = codigocliente;
	}
	public String getCodigocontrato() {
		return codigocontrato;
	}
	public void setCodigocontrato(String codigocontrato) {
		this.codigocontrato = codigocontrato;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getValorpago() {
		return valorpago;
	}
	public void setValorpago(String valorpago) {
		this.valorpago = valorpago;
	}
	
	
}
