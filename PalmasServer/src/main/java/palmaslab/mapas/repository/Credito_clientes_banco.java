package palmaslab.mapas.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Credito_clientes_banco {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long id;
	
	@JsonIgnore
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Contrato= new ArrayList<String>();
	
	String Filial;
	
	
	long codigocliente;
	
	String Nome_cliente;
	
	String Nascimento;
	
	String Sexo;
	
	String Escolaridade;
	
	String endereco;
	
	String Bairro;
	
	String Cidade;
	String Estado;
	String CEP;
	String Fone;
	
	String Renda_familiar;
	
	String Renda_per_capita;
	
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Numero_pessoas= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Atividade= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Codigo= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Descricao= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Constituicao= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Valor_financiado= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Numero_parcelas= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Renovacao= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Situacao= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> BF= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Risco= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Saldo_devedor= new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> Liberacao= new ArrayList<String>();
	
	
	
	boolean gpspositionupdated = false;
	
	String data_ultima_atualizacao;
	double latitude,longitude;
	
	
	
	public String getFilial() {
		return Filial;
	}

	public void setFilial(String filial) {
		Filial = filial;
	}

	public long getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(long codigo_cliente) {
		codigocliente = codigo_cliente;
	}

	public String getNome_cliente() {
		return Nome_cliente;
	}

	public void setNome_cliente(String nome_cliente) {
		Nome_cliente = nome_cliente;
	}

	public String getNascimento() {
		return Nascimento;
	}

	public void setNascimento(String nascimento) {
		Nascimento = nascimento;
	}

	public String getSexo() {
		return Sexo;
	}

	public void setSexo(String sexo) {
		Sexo = sexo;
	}

	public String getEscolaridade() {
		return Escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		Escolaridade = escolaridade;
	}

	public void setContrato(List<String> contrato) {
		Contrato = contrato;
	}

	public void setEndereco(String endereco) {
		endereco = endereco;
	}

	public void setBairro(String bairro) {
		Bairro = bairro;
	}

	public void setCidade(String cidade) {
		Cidade = cidade;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	public void setFone(String fone) {
		Fone = fone;
	}

	public void setRenda_familiar(String renda_familiar) {
		Renda_familiar = renda_familiar;
	}

	public void setRenda_per_capita(String renda_per_capita) {
		Renda_per_capita = renda_per_capita;
	}

	public void setNumero_pessoas(List<String> numero_pessoas) {
		Numero_pessoas = numero_pessoas;
	}

	public void setAtividade(List<String> atividade) {
		Atividade = atividade;
	}

	public void setCodigo(List<String> codigo) {
		Codigo = codigo;
	}

	public void setDescricao(List<String> descricao) {
		Descricao = descricao;
	}

	public void setConstituicao(List<String> constituicao) {
		Constituicao = constituicao;
	}

	public void setValor_financiado(List<String> valor_financiado) {
		Valor_financiado = valor_financiado;
	}

	public void setNumero_parcelas(List<String> numero_parcelas) {
		Numero_parcelas = numero_parcelas;
	}

	public void setRenovacao(List<String> renovacao) {
		Renovacao = renovacao;
	}

	public void setSituacao(List<String> situacao) {
		Situacao = situacao;
	}

	public void setBF(List<String> bF) {
		BF = bF;
	}

	public void setRisco(List<String> risco) {
		Risco = risco;
	}

	public void setSaldo_devedor(List<String> saldo_devedor) {
		Saldo_devedor = saldo_devedor;
	}

	public void setLiberacao(List<String> liberacao) {
		Liberacao = liberacao;
	}

	public boolean getGpspositionupdated()
	{
		return gpspositionupdated;
	}
	
	public void setGpspositionupdated(boolean gpspositionupdated) {
		this.gpspositionupdated = gpspositionupdated;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		System.out.println("latitude="+latitude);
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		System.out.println("longitude="+longitude);
		this.longitude = longitude;
	}

	public String getData_ultima_atualizacao() {
		return data_ultima_atualizacao;
	}

	public void setData_ultima_atualizacao(String data_ultima_atualizacao) {
    //    System.out.println("data_ultima_atualizacao="+data_ultima_atualizacao);
		this.data_ultima_atualizacao = data_ultima_atualizacao;
	}

	public long getId() {
		return id;
	}

	public List<String> getContrato() {
		
		return Contrato;
	}

	public void addContrato(String contrato) {
	//	System.out.println("contrato="+contrato);
		Contrato.add(contrato);
	}


	public List<String> getAtividade() {
		return Atividade;
	}

	public void addAtividade(String atividade) {
	//	System.out.println("atividade="+atividade);
		Atividade.add(atividade);
	}

	public List<String> getCodigo() {
		return Codigo;
	}

	public void addCodigo(String codigo) {
	//	System.out.println("codigo="+codigo);
		Codigo.add(codigo);
	}

	public List<String> getDescricao() {
		return Descricao;
	}

	public void addDescricao(String descricao) {
	//	System.out.println("descricao="+descricao);
		Descricao.add(descricao);
	}

	public String getEndereco() {
		return endereco;
	}

	

	public String getBairro() {
		return Bairro;
	}

	

	public String getCidade() {
		return Cidade;
	}



	public String getEstado() {
		return Estado;
	}

	

	public String getCEP() {
		return CEP;
	}

	

	public String getFone() {
		return Fone;
	}

	

	public List<String> getConstituicao() {
		return Constituicao;
	}

	public void addConstituicao(String constituicao) {
	//	System.out.println("constituicao="+constituicao);
		Constituicao.add(constituicao);
	}

	public String getRenda_familiar() {
		return Renda_familiar;
	}

	

	public List<String> getNumero_pessoas() {
		return Numero_pessoas;
	}

	public void addNumero_pessoas(String numero_pessoas) {
	//	System.out.println("numero_pessoas="+numero_pessoas);
		Numero_pessoas.add(numero_pessoas);
	}
       public void setNumero_pessoas(int position, String numero_pessoas)
	{
		Numero_pessoas.set(position, numero_pessoas);
	} 

	public String getRenda_per_capita() {
		return Renda_per_capita;
	}

	

	public List<String> getValor_financiado() {
		return Valor_financiado;
	}

	public void addValor_financiado(String valor_financiado) {
	//	System.out.println("valor_financiado="+valor_financiado);
		Valor_financiado.add(valor_financiado);
	}

	public List<String> getNumero_parcelas() {
		return Numero_parcelas;
	}

	public void addNumero_parcelas(String numero_parcelas) {
	//	System.out.println("numero_parcelas="+numero_parcelas);
		Numero_parcelas.add(numero_parcelas);
	}

	public List<String> getRenovacao() {
		return Renovacao;
	}

	public void addRenovacao(String renovacao) {
	//	System.out.println("renovacao="+renovacao);
		Renovacao.add(renovacao);
	}

	public List<String> getLiberacao() {
		return Liberacao;
	}

	public void addLiberacao(String liberacao) {
	//	System.out.println("liberacao="+liberacao);
		Liberacao.add(liberacao);
	}

	public List<String> getSituacao() {
		return Situacao;
	}

	public void addSituacao(String situacao) {
	//	System.out.println("situacao="+situacao);
		Situacao.add(situacao);
	}

	public List<String> getBF() {
		return BF;
	}

	public void addBF(String bF) {
	//	System.out.println("bF="+bF);
		BF.add(bF);
	}

	public List<String> getRisco() {
		return Risco;
	}

	public void addRisco(String risco) {
	//	System.out.println("risco="+risco);
		Risco.add(risco);
	}

	public List<String> getSaldo_devedor() {
		return Saldo_devedor;
	}

	public void addSaldo_devedor(String saldo_devedor) {
	//	System.out.println("saldo_devedor="+saldo_devedor);
		Saldo_devedor.add(saldo_devedor);
	}
	
	
}
