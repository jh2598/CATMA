package Data.UserDefinedType;

import java.io.Serializable;

public class GeneOntology implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8342199196753374228L;
	//GO db의 GO term 스키마를 바탕으로 함
	private int id;
	private String go_id;
	private String term;
	private String ontology;
	private String definition;
	public boolean visited;
	public GeneOntology(int id, String go_id, String term, String ontology, String definition){
		this.id = id;
		this.go_id = go_id;
		this.term = term;
		this.ontology = ontology;
		this.definition = definition;
		this.visited = false;
	}
	public int getId() {
		return id;
	}
	public String getTerm(){
		return term;
	}
	public String getOntology() {
		return ontology;
	}
	public String getDefinition() {
		return definition;
	}
	public String getGo_id() {
		return go_id;
	}
}
