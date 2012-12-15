package prestashop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DbConnector {
	String dbClass = "com.mysql.jdbc.Driver";

	Connection conn = null;

	private String HandlePassword(String pass) {
		String password = pass;
		if (pass == null) {
			Scanner in = new Scanner(System.in);
			System.out.println("Podaj haslo: ");
			password = in.nextLine();
			in.close();
		}
		return password;
	}

	public boolean open(String db, String login, String passwd) {
		System.out.println("Lacze sie z baza danych: " + db);

		String password = HandlePassword(passwd);

		try {
			Class.forName(dbClass).newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://" + db,
					login, password);

		} catch (Exception e)//| IllegalAccessException
				//| ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Nie udalo sie nawiazac polaczenia z baza");
			return false;
		}

		return true;
	}

	public int execute(String sql) {
		int result = -1;
		try {
			Statement statement = conn.createStatement();
			result = statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("Blad podczas wykonywania zapytania sql: " + sql);
		}
		return result;
	}

	public ResultSet executeSelect(String sql) {
		ResultSet result = null;
		try {
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("Blad podczas wykonywania zapytania sql: " + sql);
		}
		return result;
	}

	public void close() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Blad podczas zamykania polaczenia z baza danych");
			}
		}
	}
	
	public static void main(String [] args)
	{
		System.out.println("Test db connector");
		DbConnector db = new DbConnector();
		//db.open();
		
		int value = db.execute("UPDATE ps_category_dup SET id_shop_default=1");
		System.out.println("Result of command: "+value);
		
		ResultSet result = db.executeSelect("SELECT * FROM ps_lang");
		try {
			while (result.next()) {
				System.out.println("ISO CODE: "+result.getString("iso_code")+" ID: "+result.getInt("id_lang"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL ERROR "+e.getMessage());
		}
		
		db.close();
	}
}
