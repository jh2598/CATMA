package Data.UserDefinedType;

import java.io.Serializable;

public class GeneOntology extends Ontology implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8342199196753374228L;
	//GO db의 GO term 스키마를 바탕으로 함
	protected int id;
	protected String goId;
	protected String term;
	protected String ontology;
	protected String definition;
	public boolean visited;
	public GeneOntology(int id, String GoID, String term, String ontology, String definition){
		super(GoID);
		this.id = id;
		this.goId = GoID;
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
		return goId;
	}
}
