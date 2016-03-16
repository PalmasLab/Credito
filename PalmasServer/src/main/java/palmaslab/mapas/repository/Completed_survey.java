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
public class Completed_survey {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ElementCollection(fetch=FetchType.EAGER)
	List<String> answer = new ArrayList<String>();
	
	public Completed_survey()
	{
		
	}

	public List<String> getAnswer() 
	{
		return answer;
	}


	public void setAnswer(List<String> answer) 
	{
		this.answer = answer;
	}


	public void addAnswer(String onceAnswer)
	{
		answer.add(onceAnswer);
	}
	public void deleteAnswer(int position)
	{
		answer.remove(position);
	}
	public void deleteAllAnswer()
	{
		answer.removeAll(answer);
	}

	public long getId() {
		return id;
	}
}
