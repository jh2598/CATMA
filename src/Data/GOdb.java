package Data;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Data.UserDefinedType.GeneOntology;
import Data.UserDefinedType.Ontology;

public class GOdb implements Serializable{
	/**
	 * 여기저기서 참조하기 쉽도록 singleton으로 만들었는데 그러면 동시에 두가지 작업이 안 됨.
	 * TODO:유일한 객체가 존재하도록 하지 말고 스레드를 새로 만들어서 돌릴 수 있또록 해야함
	 */
	private static final long serialVersionUID = -220889749439050555L;
	static Connection conn = null;
	static Statement stmt = null;
	String sql;
	private static GOdb instance = null;

	//Ontology 구분을 위해 아무 값이나 할당, retrieveParentsOf에서 사용됨.
	public static final int BP = 1;
	public static final int CC = 2;
	public static final int MF = 3;
	public static final String BP_STRING = "BP";
	public static final String CC_STRING = "CC";
	public static final String MF_STRING = "MF";
	
	//	public GOdb(String GOdb_Path){
	//		try {
	//			//Register JDBC driver
	//			Class.forName("org.sqlite.JDBC");
	//			//Open a connection
	//			conn = DriverManager.getConnection("jdbc:sqlite:"+GOdb_Path);
	//			conn.setAutoCommit(false);
	//			System.out.println("GO.db :: Opened GO.db successfully");
	//			//Execute the query
	//			stmt = conn.createStatement();
	//			System.out.println("GO.db :: Create Statement.");
	//			//stmt.close();
	//			//conn.close();
	//			//System.out.println("GO.db :: Operation done successfully");
	//		}catch(SQLException se){
	//			//Handle errors for JDBC
	//			se.printStackTrace();
	//		}catch (Exception e ) {
	//			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	//			e.printStackTrace();
	//			System.exit(0);
	//		}
	//	}
	private GOdb(){
		try {
			//Register JDBC driver
			Class.forName("org.sqlite.JDBC");
			//Open a connection
			conn = DriverManager.getConnection("jdbc:sqlite:"+DataProcess.getDBPath());
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
		System.out.println("instance:"+instance);
	}
	//Ontology 생성에 참조되는 GOdb 객체는 하나만 생성되도록. 그 외에는 정상적으로 객체가 생성됨.
	public static GOdb getInstance(){
		if(instance == null){
			instance = new GOdb();
			return instance;
		}
		return instance;
	}
	//	public GOdb(String GOdb_Path){
	//		try {
	//			//Register JDBC driver
	//			Class.forName("org.sqlite.JDBC");
	//			//Open a connection
	//			conn = DriverManager.getConnection("jdbc:sqlite:"+GOdb_Path);
	//			conn.setAutoCommit(false);
	//			System.out.println("GO.db :: Opened GO.db successfully");
	//			//Execute the query
	//			stmt = conn.createStatement();
	//			System.out.println("GO.db :: Create Statement.");
	//
	//			//stmt.close();
	//			//conn.close();
	//			//System.out.println("GO.db :: Operation done successfully");
	//		}catch(SQLException se){
	//			//Handle errors for JDBC
	//			se.printStackTrace();
	//		}catch (Exception e ) {
	//			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	//			e.printStackTrace();
	//			System.exit(0);
	//		}
	//	}
	public GeneOntology[] getAllTerm() throws SQLException{
		String query = "SELECT * FROM go_term;";
		ResultSet rs = stmt.executeQuery(query);
		ArrayList<GeneOntology> goList = new ArrayList<GeneOntology>();	
		while ( rs.next() ) {
			int id = rs.getInt("_id");
			String go_id = rs.getString("go_id");
			String term = rs.getString("term");
			String ontology = rs.getString("ontology");
			String definition = rs.getString("definition");		
			goList.add(new GeneOntology(id,go_id,term,ontology,definition));
		}
		System.out.println("selecting AllTerm is done.");
		GeneOntology[] go = new GeneOntology[goList.size()];
		go = goList.toArray(go);
		rs.close();
		return go;
	}

	//GO ID를 받아서 해당 GO가 어떤 Ontology Type(BP, CC, MF)인지 반환해준다.
	public int getOntologyTypeOf(String GoID) throws SQLException{
		String query = "SELECT ontology FROM go_term WHERE go_id = '"+GoID+"';";
		ResultSet rs = stmt.executeQuery(query);
		String ontologyType = rs.getString("ontology");
		return getOntologyTypeInteger(ontologyType);
	}
	//GO ID를 받아서 그에 해당하는 GO.db의 go_term의 _id를 반환
	public int getIdFromGoId(String GO_id) throws SQLException{
		String query = "SELECT _id FROM go_term WHERE go_id = '"+GO_id+"';";
		ResultSet rs = stmt.executeQuery(query);
		int id = rs.getInt("_id");
		return id;
	}
	//ID를 받아서 GO ID를 반환
	public String getGoIdFromId(int id) throws SQLException{
		String query = "SELECT go_id FROM go_term WHERE _id = '"+id+"';";
		ResultSet rs = stmt.executeQuery(query);
		String goId = rs.getString("go_id");
		return goId;
	}
	//bpChildren, ccChildren, mfChildren에서 자신의 Ontology를 인자로 삼아 호출
	//각 Ontology에 맞게 DB의 go_XX_parents에서 인자로 전달받은 id를 부모로 가지는 term을 찾아서 그 term들의 id 전체를 배열로 반환
	public int[] retrieveChildrenOf(int ontologyType, int parentId) throws SQLException{	
		String ontology = changeOntologyTypeToTableColumn(ontologyType);
		ArrayList<Integer> childrenList = new ArrayList<>();//정수 배열 반환을 위한 임시 리스트
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

	//retrieveChildrenOf와 함께 쓰임
	//같은 순서로 is-a 관계인지 part-of 관계인지 그 외인지를 반환한다.
	public String[] retrieveRelationshipOf(int ontologyType, int parentId) throws SQLException {
		String ontology = changeOntologyTypeToTableColumn(ontologyType);
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
	//id
	public int[] retrieveParentOf(int ontologyType, int id) throws SQLException{	
		String ontology = changeOntologyTypeToTableColumn(ontologyType);
		String query = "SELECT _parent_id FROM "+ontology+" WHERE _id = "+id+";";
		ResultSet rs = stmt.executeQuery(query);
		ArrayList<Integer> parentList = new ArrayList();
		while(rs.next()){
			int parentId = rs.getInt("_parent_id");
			parentList.add(parentId);
		}
		int[] parentsId = new int[parentList.size()];
		for(int i=0;i<parentList.size();i++){
			parentsId[i] = parentList.get(i);
		}
		rs.close();
		return parentsId;		
	}
	public String[] retrieveRelationshipOfParent(int ontologyType, int id, int[] parentId) throws SQLException {
		String ontology = changeOntologyTypeToTableColumn(ontologyType);
		ArrayList<String> relationshipList = new ArrayList();
		for(int i=0;i<parentId.length;i++){
			String query = "SELECT relationship_type FROM "+ontology+" WHERE _parent_id = "+parentId[i]+" AND _id = "+id+";";
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			String relationship = rs.getString("relationship_type");
			relationshipList.add(relationship);
		}
		String[] relationships = new String[relationshipList.size()];
		for(int i=0;i<relationshipList.size();i++){
			relationships[i] = relationshipList.get(i).toString();
		}
		return relationships;
	}


	public static String changeOntologyTypeToTableColumn(int ontologyType){
		//GOdb.BP 등의 static 변수를 인자로 받아서 그에 해당하는 GO.db의 column의 문자열 반환
		String ontology = null;//Ontology Type에 따른 임시변수
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
	public static int getOntologyTypeInteger(String ontologyType){
		//반환값이 -1이면 실패.
		if(ontologyType.compareTo("BP")==0){
			return GOdb.BP;
		}else if(ontologyType.compareTo("CC")==0){
			return GOdb.CC;
		}else if(ontologyType.compareTo("MF")==0){
			return GOdb.MF;
		}else{
			System.err.println("ONTOLOGY TYPE:"+ontologyType+"? <- Ontology type is invalid.");
			return -1;
		}
	}
	public static boolean isRoot(String GoId){
		if(GoId.compareTo("GO:003674")==0){
			return true;
		}
		if(GoId.compareTo("GO:005575")==0){
			return true;
		}
		if(GoId.compareTo("GO:008150")==0){
			return true;
		}
		return false;
	}
	public static boolean isRoot(Ontology geneOntology){
		if(geneOntology.getGoId().compareTo("GO:003674")==0){
			return true;
		}
		if(geneOntology.getGoId().compareTo("GO:005575")==0){
			return true;
		}
		if(geneOntology.getGoId().compareTo("GO:008150")==0){
			return true;
		}
		return false;
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
