package Data;

public class GO {
	private int id;
	private String go_id;
	private String term;
	private String ontology;
	private String definition;
	public boolean visited;
	public GO(int id, String go_id, String term, String ontology, String definition){
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
