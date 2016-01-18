package Data;
import java.sql.*;

public class GOdb {
	public GOdb(String GOdb_Path){
		//read GO.sqlite
		Connection conn = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }

	}
}
