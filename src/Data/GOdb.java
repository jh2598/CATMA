package Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GOdb {
	Connection conn = null;
	Statement stmt = null;
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
	public GO[] getAllTerm() throws SQLException{
		ResultSet rs = stmt.executeQuery( "SELECT * FROM go_term;" );
		ArrayList<GO> goList = new ArrayList<GO>();	
		while ( rs.next() ) {
			int id = rs.getInt("_id");
			String go_id = rs.getString("go_id");
			String term = rs.getString("term");
			String ontology = rs.getString("ontology");
			String definition = rs.getString("definition");
			goList.add(new GO(id,go_id,term,ontology,definition));
		}
		GO[] go = new GO[goList.size()];
		go = goList.toArray(go);
		rs.close();
		return go;
	}
	public void selectBpOffspring() throws SQLException{
		//go_term print
				ResultSet rs = stmt.executeQuery( "SELECT * FROM go_bp_offspring;" );
				while ( rs.next() ) {
					int id = rs.getInt("_id");
					String _offspring_id = rs.getString("_offspring_id");
					System.out.println( "id = " + id );
					System.out.println( "offspring_id = " + _offspring_id );
					System.out.println();
				}
				rs.close();
	}
	public int[] getBpOffspring(int id) throws SQLException{
		//id에 해당하는 GO term의 offspring의 id들을 반환
		ArrayList<Integer> offspringList = new ArrayList<Integer>();
		ResultSet rs = stmt.executeQuery( "SELECT _offspring_id FROM go_bp_offspring WHERE _id = "+id+";" );
		while ( rs.next() ) {
			int _offspring_id = rs.getInt("_offspring_id");
			offspringList.add(_offspring_id);
		}
		int[] offsprings = new int[offspringList.size()];
		for(int i=0;i<offspringList.size();i++){
			offsprings[i] = offspringList.get(i).intValue();
		}
		rs.close();
		return offsprings;
	}
	public int[] getCcOffspring(int id) throws SQLException{
		//id에 해당하는 GO term의 offspring의 id들을 반환
		ArrayList<Integer> offspringList = new ArrayList<Integer>();
		ResultSet rs = stmt.executeQuery( "SELECT _offspring_id FROM go_cc_offspring WHERE _id = "+id+";" );
		while ( rs.next() ) {
			int _offspring_id = rs.getInt("_offspring_id");
			offspringList.add(_offspring_id);
		}
		int[] offsprings = new int[offspringList.size()];
		for(int i=0;i<offspringList.size();i++){
			offsprings[i] = offspringList.get(i).intValue();
		}
		rs.close();
		return offsprings;
	}
	public int[] getMfOffspring(int id) throws SQLException{
		//id에 해당하는 GO term의 offspring의 id들을 반환
		ArrayList<Integer> offspringList = new ArrayList<Integer>();
		ResultSet rs = stmt.executeQuery( "SELECT _offspring_id FROM go_mf_offspring WHERE _id = "+id+";" );
		while ( rs.next() ) {
			int _offspring_id = rs.getInt("_offspring_id");
			offspringList.add(_offspring_id);
		}
		int[] offsprings = new int[offspringList.size()];
		for(int i=0;i<offspringList.size();i++){
			offsprings[i] = offspringList.get(i).intValue();
		}
		rs.close();
		return offsprings;
	}
}
