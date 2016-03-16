package palmaslab.mapas.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LinhaNegPagamento {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	String dataalerta;
	String valorparcela;
	String valorrecebido;
	String comentario;
	boolean checked = false;
	boolean alertaativa = false;
	
	long idnegociacao;
	
	public LinhaNegPagamento()
	{
		
	}
	

	public boolean isAlertaativa() {
		return alertaativa;
	}


	public void setAlertaativa(boolean alertaativa) {
		this.alertaativa = alertaativa;
	}


	public void setId(long id) {
		this.id = id;
	}

	public String getDataalerta() {
		return dataalerta;
	}

	public void setDataalerta(String string) {
		this.dataalerta = string;
	}



	public String getValorparcela() {
		return valorparcela;
	}

	public void setValorparcela(String valorparcela) {
		this.valorparcela = valorparcela;
	}

	public String getValorrecebido() {
		return valorrecebido;
	}

	public void setValorrecebido(String valorrecebido) {
		this.valorrecebido = valorrecebido;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public long getIdnegociacao() {
		return idnegociacao;
	}

	public void setIdnegociacao(long idnegociacao) {
		this.idnegociacao = idnegociacao;
	}

	public long getId() {
		return id;
	}
	
}
