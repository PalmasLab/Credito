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
import com.google.common.base.Objects;


@Entity
public class Postosaude {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long id;
	
	private String nome_posto_saude;
	private double latitude;
	private double longitude;
	private String endereco;
	private String barrio_ou_nome_municipio; 
	private String cidade;
	private String estado;
	private String pais; 
	private int PicturesCount;
	private String descricao;
	private long project_id;
	
	
	//MUDAR NOMBRE PARA lista_respostas_dos_parametros
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> lista_respostas = new ArrayList<String>();
	
	@JsonIgnore
	@ElementCollection(fetch=FetchType.EAGER) 
	List<String> YoutubeLinkVideos = new ArrayList<String>();
	
	@ElementCollection(fetch=FetchType.EAGER) 
	List<Completed_survey> list_completed_survey = new ArrayList<Completed_survey>();
	


	public Postosaude()
	{}
	
	public Postosaude(String nome_posto_saude, String endereco,String barrio_ou_nome_municipio,String cidade, String estado, String pais, List<String> lista_respostas_dos_parametros, int PicturesCount,String descricao,long project_id)
	{
		this.project_id = project_id;
		this.descricao = descricao;
		this.nome_posto_saude = nome_posto_saude;
		this.endereco = endereco;
		this.barrio_ou_nome_municipio = barrio_ou_nome_municipio;
		this.cidade = cidade;
		this.estado = estado;
		this.pais = pais;
		this.lista_respostas = lista_respostas_dos_parametros;
		this.PicturesCount=PicturesCount;
		tirarAcentos();
	}
	private void tirarAcentos()
	{
		
	}
	
	public long getProject_id() {
		return project_id;
	}

	public void setProject_id(long project_id) {
		this.project_id = project_id;
	}
	
	public List<Completed_survey> getList_completed_survey() {
		return list_completed_survey;
	}

	public void setList_completed_survey(
			List<Completed_survey> list_completed_survey) {
		this.list_completed_survey = list_completed_survey;
	}

	public void addCompleted_survey(Completed_survey mCompleted_survey)
	{
		list_completed_survey.add(mCompleted_survey);
	}
	public void addListCompleted_survey(List<Completed_survey> mListCompleted_survey)
	{
		list_completed_survey.addAll(mListCompleted_survey);
	}
	
	public void setOneCompleted_Survey (Completed_survey mCompleted_survey)
	{
		for(int i=0; i<list_completed_survey.size();i++)
		{
			if(list_completed_survey.get(i).getId() == mCompleted_survey.getId())
			{
				list_completed_survey.set(i, mCompleted_survey);
			}
		}
		
	}
	public  Completed_survey getOneCompleted_survey(long id)
	{
		Completed_survey mCompleted_survey=null;
		
		for(int i=0; i<list_completed_survey.size();i++)
		{
			if(list_completed_survey.get(i).getId() ==id)
			{
				mCompleted_survey = list_completed_survey.get(i);
			}
		}
		return mCompleted_survey;
	}
	public void deleteOneCompleted_survey(int id)
	{
		for(int i=0; i<list_completed_survey.size();i++)
		{
			if(list_completed_survey.get(i).getId() == id)
			{
				list_completed_survey.remove(i);
			}
		}
		
	}
	
	public void deleteAllCompleted_survey()
	{
		list_completed_survey.removeAll(list_completed_survey);
	}
	public void add_youtube_link(String youtube_link)
	{
		YoutubeLinkVideos.add(youtube_link);
	}
	public void delete_youtube_link(String youtube_link)
	{
		
	}
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<String> getYoutubeLinkVideos() {
		return YoutubeLinkVideos;
	}

	public void setYoutubeLinkVideos(List<String> youtubeLinkVideos) {
		YoutubeLinkVideos = youtubeLinkVideos;
	}

	public int getPicturesCount() {
		return PicturesCount;
	}

	public void setPicturesCount(int picturesCount) {
		PicturesCount = picturesCount;
	}

	public List<String> getLista_respostas() {
		return lista_respostas;
	}

	public void setLista_respostas(List<String> lista_respostas) {
		this.lista_respostas = lista_respostas;
	}

	public String getBarrio_ou_nome_municipio() {
		return barrio_ou_nome_municipio;
	}

	public void setBarrio_ou_nome_municipio(String barrio_ou_nome_municipio) {
		this.barrio_ou_nome_municipio = barrio_ou_nome_municipio;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getNome_posto_saude() {
		return nome_posto_saude;
	}

	public void setNome_posto_saude(String nome_posto_saude) {
		this.nome_posto_saude = nome_posto_saude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double d) {
		this.latitude = d;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double d) {
		this.longitude = d;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public long getId() {
		return id;
	}
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(nome_posto_saude, endereco, cidade);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Postosaude) {
			Postosaude other = (Postosaude) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(nome_posto_saude, other.nome_posto_saude)
					&& Objects.equal(endereco, other.endereco)
					&& cidade == other.cidade;
		} else {
			return false;
		}
	}
	
}
