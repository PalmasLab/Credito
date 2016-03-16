package palmaslab.mapas.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;


@Entity
public class survey_question {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ElementCollection(fetch=FetchType.EAGER)
	List<String> rrespostas = new ArrayList<String>();
	
	public String pergunta;
	public survey_question()
	{}
	public long getId()
	{
		return id;
	}
	public survey_question(@JsonProperty("pergunta")String pergunta,@JsonProperty("respostas")List<String> rrespostas)
	{
		this.pergunta = pergunta;
		this.rrespostas = rrespostas;
		Gson gson = new Gson();
		gson.toJson(rrespostas);
	}
	public void addResposta(String resposta)
	{
		rrespostas.add(resposta);
	}
	public void removeResposta(int i)
	{
		rrespostas.remove(i);
	}
	
	

	public List<String> getRrespostas() {
		return rrespostas;
	}
	public void setRrespostas(List<String> rrespostas) {
		this.rrespostas = rrespostas;
	}
	public String getPergunta() {
		return pergunta;
	}
	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}
	
	
}
