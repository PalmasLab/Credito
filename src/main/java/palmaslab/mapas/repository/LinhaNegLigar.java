package palmaslab.mapas.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LinhaNegLigar {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	String dataalerta;
	String comentario;
	boolean checked = false;
	boolean alertaativa = false;
	
	long idnegociacao;
	
	public LinhaNegLigar()
	{}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDataalerta() {
		return dataalerta;
	}

	public void setDataalerta(String dataalerta) {
		this.dataalerta = dataalerta;
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


	public boolean isAlertaativa() {
		return alertaativa;
	}

	public void setAlertaativa(boolean alertaativa) {
		this.alertaativa = alertaativa;
	}


	public long getIdnegociacao() {
		return idnegociacao;
	}

	public void setIdnegociacao(long idnegociacao) {
		this.idnegociacao = idnegociacao;
	}
	
	
}
