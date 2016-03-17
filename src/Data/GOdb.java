package Data;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Data.UserDefinedType.GeneOntology;

public class GOdb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -220889749439050555L;
	static Connection conn = null;
	static Statement stmt = null;
	String sql;
	public GOdb(String GOdb_Path){
		try {
			//Register JDBC driver
			Class.forName("org.sqlite.JDBC");
			//Open a connection
			conn = DriverManager.getConnection("jdbc:sqlite:"+GOdb_Path);
			conn.setAutoCommit(false);
			System.out.println("GO.db :: Opened GO.db successfully");
			//Execute the query
			stmt = conn.createStatement();
			System.out.println("GO.db :: Create Statement.");

			//stmt.close();
			//conn.close();
			//System.out.println("GO.db :: Operation done successfully");
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch (Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}
	}
	public void selectOntology() throws SQLException{
		//go_ontology print
		sql = "SELECT * FROM go_ontology;";
		System.out.println("GO.db :: Query : "+sql);
		ResultSet rs = stmt.executeQuery(sql);
		System.out.println("GO.db :: The query is executed.");
		while ( rs.next() ) {
			int ontology = rs.getInt("ontology");
			String  term_type = rs.getString("term_type");
			System.out.println( "Ontology = " + ontology );
			System.out.println( "Term Type = " + term_type );
			System.out.println();
		}
		rs.close();
	}
	public void selectTerm() throws SQLException{
		//go_term print
		ResultSet rs = stmt.executeQuery( "SELECT * FROM go_term;" );
		while ( rs.next() ) {
			int id = rs.getInt("_id");
			String go_id = rs.getString("go_id");
			String term = rs.getString("term");
			String ontology = rs.getString("ontology");
			String definition = rs.getString("definition");
			System.out.println( "id = " + id );
			System.out.println( "go_id = " + go_id );
			System.out.println( "term = " + term);
			System.out.println( "ontology = " + ontology);
			System.out.println( "definition = " + definition);
		}
		rs.close();
	}
	public GeneOntology[] getAllTerm() throws SQLException{
		ResultSet rs = stmt.executeQuery( "SELECT * FROM go_term;" );
		ArrayList<GeneOntology> goList = new ArrayList<GeneOntology>();	
		while ( rs.next() ) {
			int id = rs.getInt("_id");
			String go_id = rs.getString("go_id");
			String term = rs.getString("term");
			String ontology = rs.getString("ontology");
			String definition = rs.getString("definition");
			goList.add(new GeneOntology(id,go_id,term,ontology,definition));
		}
		GeneOntology[] go = new GeneOntology[goList.size()];
		go = goList.toArray(go);
		rs.close();
		return go;
	}
	//Ontology ������ ���� �ƹ� ���̳� �Ҵ�, retrieveParentsOf���� ����.
	static final int BP = 1;
	static final int CC = 2;
	static final int MF = 3;
	//bpChildren, ccChildren, mfChildren���� �ڽ��� Ontology�� ���ڷ� ��� ȣ��
	//�� Ontology�� �°� DB�� go_XX_parents���� ���ڷ� ���޹��� id�� �θ�� ������ term�� ã�Ƽ� �� term���� id ��ü�� �迭�� ��ȯ
	public int[] retrieveChildrenOf(int ontologyType, int parentId) throws SQLException{	
		String ontology = getOntologyTypeString(ontologyType);
		ArrayList<Integer> childrenList = new ArrayList<>();//���� �迭 ��ȯ�� ���� �ӽ� ����Ʈ
		String query = "SELECT _id FROM "+ontology+" WHERE _parent_id = "+parentId+";";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){//Add child into childrenList(Temporary)
			int _children_id = rs.getInt("_id");
			childrenList.add(_children_id);
		}
		int[] children = new int[childrenList.size()];
		for(int i=0;i<childrenList.size();i++){
			children[i] = childrenList.get(i).intValue();
		}
		rs.close();
		if(children.length == 0){
			//System.err.println("term(_id:"+parentId+") of go_term has no child.");
		}
		return children;		
	}
	//retrieveChildrenOf�� �Բ� ����
	//���� ������ is-a �������� part-of �������� �� �������� ��ȯ�Ѵ�.
	public String[] retrieveRelationshipOf(int ontologyType, int parentId) throws SQLException {
		String ontology = getOntologyTypeString(ontologyType);
		ArrayList<String> relationshipList = new ArrayList<>();
		String query = "SELECT relationship_type FROM "+ontology+" WHERE _parent_id = "+parentId+";";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			String relationship = rs.getString("relationship_type");
			relationshipList.add(relationship);
		}
		String[] relationships = new String[relationshipList.size()];
		for(int i=0;i<relationshipList.size();i++){
			relationships[i] = relationshipList.get(i).toString();
		}
		return relationships;
	}
	public String getOntologyTypeString(int ontologyType){
		String ontology = null;//Ontology Type�� ���� �ӽú���
		if(ontologyType == BP){
			ontology = "go_bp_parents";
		}else if(ontologyType == CC){
			ontology = "go_cc_parents";
		}else if(ontologyType == MF){
			ontology = "go_mf_parents";
		}else{
			System.err.println("retrieveChildrenOf ONTOLOGY TYPE:"+ontologyType+"? <- Ontology type is invalid.");
		}
		return ontology;
	}
	
	public int getMfRootId() throws SQLException{
		ResultSet rs = stmt.executeQuery( "SELECT _id FROM go_term WHERE go_id = "+"'GO:0003674'"+";" );
		ArrayList<Integer> arr = new ArrayList<Integer>();
		int _id = -1;
		while ( rs.next() ) {
			_id = rs.getInt("_id");
		}
		return _id;
	}
}
