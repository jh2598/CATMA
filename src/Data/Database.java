package Data;

import java.io.Serializable;
import java.sql.*;
public class Database implements Serializable{
	private static final long serialVersionUID = -8151450629209385190L;
	Connection conn = null;
	Statement stmt = null;
	String SAMPLE = "SAMPLE";
	String sql;
	DataProcess dataProcess;
	public Database(DataProcess dataProcess){
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("SQLite :: Opened database successfully");
		this.dataProcess = dataProcess;
	}
	public void saveSample(){
		createSampleTable();
		insertSampleTable();
	}
	public void createSampleTable(){
		try {
			//Read Sample Info from R
			String[] sampleName = dataProcess.getSampleNames();

			//Table Create.
			stmt = conn.createStatement();
			sql = "DROP TABLE IF EXISTS "+ SAMPLE;
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE " + SAMPLE +
					"(ID INT PRIMARY KEY NOT NULL"+
					", PROVE TEXT NOT NULL"+
					", ENTREZ TEXT NOT NULL"+
					", SYMBOL TEXT NOT NULL";
			for (String str : sampleName) {
				str = str.replace(".CEL", "");
				sql = sql + ", '"+str+"' TEXT";
			}
			sql = sql + ")";
//			System.out.println(sql);
			System.out.println("SQLite :: Created Table successfully");
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	public void insertSampleTable(){
		int idx = dataProcess.getSampleLength();
		int num = dataProcess.getSampleNumber();
		String[][] id = dataProcess.getSampleId();
		String[][] exp = dataProcess.getSampleExp();
		String str;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("SQLite :: Opened database successfully");
			stmt = c.createStatement();
			
			for(int i=0;i<idx;i++){
				str = i+1 + ", "+ "'" + id[0][i] + "', '"+ id[1][i] + "', '"+ id[2][i]+"'";
				for(int j=0;j<num;j++){
					str = str +", '"+ exp[j][i]+"'";
				}

				sql = "INSERT INTO "+SAMPLE+" VALUES ("+str+")";
				//System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			stmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("SQLite :: Records created successfully");
	}
	public void saveDEG(DataProcess data){

	}
}
