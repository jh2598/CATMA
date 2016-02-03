package Data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
public class Database implements Serializable{
	private static final long serialVersionUID = -8151450629209385190L;
	Connection conn = null;
	Statement stmt = null;
	String dbName;
	String SAMPLE = "SAMPLE";
	String sql;
	DataProcess dataProcess;
	public Database(DataProcess dataProcess){
		this.dbName = dataProcess.session.name;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("SQLite :: Opened database successfully");
		this.dataProcess = dataProcess;
	}
	public Database(String dbname){
		this.dbName = dbname;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbname+".db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("SQLite :: Opened database successfully");
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

		//Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dataProcess.session.name+".db");
			conn.setAutoCommit(false);
			System.out.println("SQLite :: Opened database successfully");
			stmt = conn.createStatement();
			
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
			conn.commit();
			conn.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("SQLite :: Records created successfully");
	}
	public String[][] retrieveSampleTable(){
		//Sample table array[Row][Column]
		stmt = null;
		String[][] sampleArray = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      conn = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
	      conn.setAutoCommit(false);
	      System.out.println("Opened database successfully");
	      stmt = conn.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM SAMPLE;" );
		  //몇개의 행이 있는지 알아냄
		  int idx = 0;
		  while(rs.next()){
			  idx++;
		  }
		  rs = null;
		  rs = stmt.executeQuery( "SELECT * FROM SAMPLE;" );
		  //metaData를 이용해 몇 개의 열이 있는지 알아냄
		  ResultSetMetaData meta = rs.getMetaData();
		  int colCount = meta.getColumnCount();
		  int rowCount = 0;
		  //반환할 문자열 배열
		  sampleArray = new String[idx][colCount];
	      while ( rs.next() ) {
	    	  try{
	    		  for(int i =0 ;i<colCount;i++){
	    			  sampleArray[rowCount][i] = rs.getString(i+1);
//	    			  System.out.print(rs.getString(i+1));
//	    			  System.out.print(",");
	    		  }
	    		  rowCount++;
//	    		  System.out.println();
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }
	      }
	      rs.close();
	      stmt.close();
	      conn.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Retrieving Sample table Operation done successfully");
		return sampleArray;
	}
	public void saveDEG(DataProcess data){
	}
}
