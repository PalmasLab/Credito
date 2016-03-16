package palmaslab.mapas.repository;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	String logginname;
	private String complete_name;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@JsonManagedReference
	List<String> projects = new ArrayList<String>();
	
	private Date creation_date;
	
	
	public Cliente()
	{}
	
	public Cliente(String logginname, String complete_name,Date creation_date)
	{
		this.logginname = logginname;
		this.complete_name = complete_name;
		this.creation_date = creation_date;
	}

	public String getLogginname() {
		return logginname;
	}

	public void setLogginname(String loggin_name) {
		this.logginname = logginname;
	}

	public String getComplete_name() {
		return complete_name;
	}

	public void setComplete_name(String complete_name) {
		this.complete_name = complete_name;
	}

	public List<String> getProjects() {
		return projects;
	}
	
	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	public long getId() {
		return id;
	}
	
	public void addProject(String project)
	{
		projects.add(project);
	}
	
	public void removeProject(int index)
	{
		projects.remove(index);
	}
	public void removeAllProjects()
	{
		
		projects.removeAll(projects);
	}
	

}
