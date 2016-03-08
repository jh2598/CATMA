package Data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
public class Database implements Serializable{
	private static final long serialVersionUID = -8151450629209385190L;
	static final String SAMPLE = "SAMPLE";
	static final String ENTREZ_GO = "ENTREZ_GO";
	Connection conn = null;
	Statement stmt = null;
	String dbName;
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
		//�� �����ڸ� ���� Retrieve �̿��� �޼ҵ�� DataProcess�� ��� NullPointerException
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
		//Table : ID, PROBE, ENTREZ, SYMBOL, SAMPLE...
		stmt = null;
		String[][] sampleArray = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
			conn.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM SAMPLE;" );
			//��� ���� �ִ��� �˾Ƴ�
			int idx = 0;
			while(rs.next()){
				idx++;
			}
			rs = null;
			rs = stmt.executeQuery( "SELECT * FROM SAMPLE;" );
			//metaData�� �̿��� �� ���� ���� �ִ��� �˾Ƴ�
			ResultSetMetaData meta = rs.getMetaData();
			int colCount = meta.getColumnCount();
			int rowCount = 0;
			//��ȯ�� ���ڿ� �迭
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
	public String[] getSampleNames(){
		//Sample Column Names
		//.Cel ���� ������ �̸���...
		String name;
		String[] names = null;
		try{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
			conn.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM SAMPLE;" );
			names = new String[rs.getMetaData().getColumnCount()];
			for(int i=0;i<rs.getMetaData().getColumnCount();i++){
				name = rs.getMetaData().getColumnLabel(i);
				names[i] = name;
			}
			rs.close();
			stmt.close();
			conn.close();
			System.out.println("Get Sample names...");
		}catch(Exception e){
			e.printStackTrace();
		}
		return names;
	}
	public void saveDEG(DataProcess data){
	}

	public void createEntrezToGoTable(){
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
			System.out.println("SQLite :: Creating ENTREZ_GO Table...");
			//Table Create.
			System.out.println(conn);
			stmt = conn.createStatement();
			System.out.println(stmt);
			sql = "DROP TABLE IF EXISTS "+ ENTREZ_GO;
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE " + ENTREZ_GO +
					"(ID INT PRIMARY KEY NOT NULL"+
					", ENTREZ TEXT NOT NULL"+
					", GO TEXT NOT NULL"+
					", EVIDENCE TEXT NOT NULL"+
					", ONTOLOGY TEXT NOT NULL";
			sql = sql + ")";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			System.out.println("SQLite :: Created ENTREZ_GO Table successfully");
			stmt.close();
			conn.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}
	}
	public void insertEntrezToGoTable(String[][] eg){
		int idx = eg.length;
		int num = eg[0].length;
		String str;
		//Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dataProcess.session.name+".db");
			conn.setAutoCommit(false);
			System.out.println("SQLite :: Opened database successfully");
			stmt = conn.createStatement();
			
			for(int i=0;i<num;i++){
				
			str = i+",'"+ eg[0][i] +"', "+"'"+ eg[1][i] +"', "+"'"+ eg[2][i] +"', "+"'"+ eg[3][i] +"'";
				sql = "INSERT INTO "+ENTREZ_GO+" VALUES ("+str+")";
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
	//Entrez id�� �޾� �׿� �ش��ϴ� GO�� ��ȯ
	public String[] entrezToGo(String entrez){
		String[] eg = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
			conn.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT GO FROM ENTREZ_GO WHERE ENTREZ = "+entrez+";");
			//��ȯ�� ���ڿ� �迭
			ArrayList<String> egList = new ArrayList<String>();
			while ( rs.next() ) {
				try{
					egList.add(rs.getString(1));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			eg = new String[egList.size()];
			eg = egList.toArray(eg);
//			for(int i=0;i<eg.length;i++){
//				System.out.println(eg[i]);
//			}
			rs.close();
			stmt.close();
			conn.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}
		return eg;
	}
	//GO�� �޾� �ش��ϴ� Entrez ID�� ��ȯ
	public String[] GoToEntrez(String go){
		String[] ge = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
			conn.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT ENTREZ FROM ENTREZ_GO WHERE GO = '"+go+"';");
			//��ȯ�� ���ڿ� �迭
			ArrayList<String> geList = new ArrayList<String>();
			while ( rs.next() ) {
				try{
					geList.add(rs.getString(1));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			ge = new String[geList.size()];
			ge = geList.toArray(ge);
//			for(int i=0;i<ge.length;i++){
//				System.out.println(ge[i]);
//			}
			rs.close();
			stmt.close();
			conn.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}
		return ge;
	}
}