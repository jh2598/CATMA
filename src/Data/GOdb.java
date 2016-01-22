package Data;
import java.sql.*;

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
			String  term = rs.getString("term");
			String ontology = rs.getString("ontology");
			String definition = rs.getString("definition");
			System.out.println( "id = " + id );
			System.out.println( "go_id = " + go_id );
			System.out.println( "term = " + term);
			System.out.println( "ontology = " + ontology);
			System.out.println( "definition = " + definition);
			System.out.println();
		}
		rs.close();
	}
}
