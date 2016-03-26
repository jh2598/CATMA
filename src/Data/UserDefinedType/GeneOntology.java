package Data.UserDefinedType;

import java.io.Serializable;

import Data.GOdb;

public class GeneOntology extends Ontology implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8342199196753374228L;
	//GO db의 GO term 스키마를 바탕으로 함
	protected int id;
	protected String goId;
	protected String term;
	protected String ontologyTypeString;
	protected String definition;
	public boolean visited;
	public GeneOntology(int id, String GoID, String term, String ontology, String definition){
		super(GoID);
		this.id = id;
		this.goId = GoID;
		this.term = term;
		this.ontologyTypeString = ontology;
		if(ontologyTypeString.compareTo(GOdb.BP_STRING)==0){
			super.setOntologyType(GOdb.BP);
		}else if((ontologyTypeString.compareTo(GOdb.CC_STRING)==0)){
			super.setOntologyType(GOdb.CC);
		}else if((ontologyTypeString.compareTo(GOdb.MF_STRING)==0)){
			super.setOntologyType(GOdb.MF);
		}else{
			super.setOntologyType(-1);
		}
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
		return ontologyTypeString;
	}
	public String getDefinition() {
		return definition;
	}
	public String getGo_id() {
		return goId;
	}
}
