package palmaslab.mapas.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;


@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	
	@ElementCollection(fetch=FetchType.EAGER)
	@JsonIgnore
	private List<String> clients_with_project = new ArrayList<String>();
	
	
	private String project_name;
	
	private Date date;
	private String nome_classe;
	
	boolean withParameters;
	

	boolean withSurvey;
	
	
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@ElementCollection(fetch=FetchType.EAGER)
	List<parametros> Parametros = new ArrayList<parametros>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	List<survey_question> Survey = new ArrayList<survey_question>();
	
	String listaperguntasjson;
	
	@Lob
	@Column( length = 100000 )
	String listarespostasjson;
	
	public Project()
	{
	}
	
	@JsonCreator
	public Project(@JsonProperty("project_name") String project_name,@JsonProperty("date")Date date,@JsonProperty("nome_classe")String nome_classe,@JsonProperty("Lista")List<parametros> Parametros)
	{
		this.project_name = project_name;
		this.date = date;
		this.nome_classe = nome_classe;
		
		this.Parametros = Parametros;

		
		Gson gson = new Gson();
	//this.listaperguntasjson = gson.toJson(Listaperguntas);
	this.listarespostasjson = gson.toJson(Parametros);
	}
	
	public List<survey_question> getAllSurveyQuestions()
	{
		for( int i=0; i< Survey.size();i++)
		{
			System.out.println("Ids de los Surveys salvados del projecto="+Survey.get(i).getId());
		}
		return Survey;
	}
	public void addSurveyQuestion (survey_question SurveyQuestion)
	{
	//	if(isWithSurvey())
		{
			Survey.add(SurveyQuestion);
		}
	}
	public void addAllSurveyQuestion (List<survey_question> Survey)
	{
	//	if(isWithSurvey())
		{
			Survey.addAll(Survey);
		}
	}
	public void deleteSurveyQuestion(int position)
	{
	//	if(isWithSurvey())
		{
			Survey.remove(position);
		}
	}
	public void deleteAllSurvey()
	{
	//	if(isWithSurvey())
		{
			Survey.removeAll(Survey);
		}
	}
	public boolean isWithParameters() {
		return withParameters;
	}

	public void setWithParameters(boolean withParameters) {
		this.withParameters = withParameters;
	}

	public boolean isWithSurvey() {
		return withSurvey;
	}

	public void setWithSurvey(boolean withSurvey) {
		this.withSurvey = withSurvey;
	}

	public void addClient_to_the_project(String cliente)
	{
		clients_with_project.add(cliente);
	}
	
	public void removeClient_in_this_project(int index)
	{
		clients_with_project.remove(index);
	}
	public void removeAllClients_in_this_project()
	{
		clients_with_project.removeAll(clients_with_project);
	}
	
	public void removeParametros()
	{
		Parametros.removeAll(Parametros);
	}
	
	public List<String> getClients_with_project() {
		return clients_with_project;
	}

	

	public void setClients_with_project(List<String> clients_with_project) {
		this.clients_with_project = clients_with_project;
	}


	public String getProject_name() {
		return project_name;
	}


	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public long getId() {
		return id;
	}





	public List<parametros> getLista() {
		//return null;
		return Parametros;
	}

	public void setLista(List<parametros> lista) {
		Parametros = lista;
	}
	public void setOneEditedSurvey (survey_question msurvey_question)
	{
		for(int i=0; i<Survey.size();i++)
		{
			if(Survey.get(i).getId() == msurvey_question.getId())
			{
				Survey.set(i, msurvey_question);
			}
		}
		
	}
	public  survey_question getOneEditedSurvey(int position)
	{
		return Survey.get(position);
	}
	public void deleteOneEditedSurvey(int id_survey)
	{
		for(int i=0; i<Survey.size();i++)
		{
			if(Survey.get(i).getId() == id_survey)
			{
				Survey.remove(i);
			}
		}
		
	}
	public List<parametros> getAllParametersQuestions()
	{
		for( int i=0; i< Parametros.size();i++)
		{
			System.out.println("Ids de los Parametros salvados del projecto="+Parametros.get(i).getId());
		}
		return Parametros;
	}
	public void addParametrosQuestion (parametros Mparametros)
	{
	//	if(isWithSurvey())
		{
			Parametros.add(Mparametros);
		}
	}
	public void addAllParametrosQuestion (List<parametros> Parametros)
	{
	//	if(isWithSurvey())
		{
			Parametros.addAll(Parametros);
		}
	}
	
	public void deleteAllParameters()
	{
	//	if(isWithSurvey())
		{
			Parametros.removeAll(Parametros);
		}
	}
	public void setOneEditedParameter (parametros mparametros)
	{
		for(int i=0; i<Parametros.size();i++)
		{
			if(Parametros.get(i).getId() == mparametros.getId())
			{
				Parametros.set(i, mparametros);
			}
		}
		
	}
	public  survey_question getOneEditedParametro(int position)
	{
		return Survey.get(position);
	}
	public void deleteOneEditedParametro(int id_parametro)
	{
		for(int i=0; i<Parametros.size();i++)
		{
			if(Parametros.get(i).getId() == id_parametro)
			{
				Parametros.remove(i);
			}
		}
		
	}
/*
	public String getListaperguntasjson() {
		return listaperguntasjson;
	}

	public void setListaperguntasjson(String listaperguntasjson) {
		this.listaperguntasjson = listaperguntasjson;
	}

	public String getListarespostasjson() {
		return listarespostasjson;
	}

	public void setListarespostasjson(String listarespostasjson) {
		this.listarespostasjson = listarespostasjson;
	}*/

}
