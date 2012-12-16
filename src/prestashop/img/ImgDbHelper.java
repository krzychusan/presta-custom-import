package prestashop.img;

import java.sql.ResultSet;
import java.sql.SQLException;

import prestashop.database.DbConnector;

public class ImgDbHelper {
	
	private DbConnector dbConnector;
	private String languageId = "7" ;
	private String shopId = "1";
	private String user = "";
	private String passwd = "";
	private String db = "";
	
	public ImgDbHelper(String user, String passwd, String db) {
		dbConnector = new DbConnector();
		this.user = user;
		this.passwd = passwd;
		this.db = db;
	}
	
	public void open(){
		dbConnector.open(db, user, passwd);
	}
	
	public void close(){
		dbConnector.close();
	}
	 
	public String getDbProductId(String name) {
			ResultSet result = dbConnector.executeSelect("select id_product from ps_product_lang" +
					                  " where name = \"" + name + "\" and " +
					                  "id_shop = " + shopId + " and  id_lang = " + languageId + ";");
			try {
				if (result.next()) {
					return String.valueOf(result.getInt(1));
				} else
					return null;
			} catch (SQLException e) {
				System.out.println("Unable to get product id!");
				e.printStackTrace();
				return null;
			}
	}
	
	public String getImgIdForDbProductName(String name){
		String imgId = null;
		String productId = this.getDbProductId(name);
		if (productId == null) 
			return null;
		ResultSet result = dbConnector.executeSelect("select id_image from ps_image " +
				                                     "where id_product = " + productId +
				                                     " limit 1;");
		try {
			if (result.next())
				imgId = String.valueOf(result.getInt(1));
		} catch (SQLException e) {
			System.out.println("Unable to get image id!");
			e.printStackTrace();
		} 
		return imgId;
		
	}
	
	public String insertPicture(String dbProductId, String productName){
		String pictureId = "";
		int picId = 0;
		try {
			dbConnector.execute("insert into ps_image(id_product, position, cover) values(" +  dbProductId + ",1,1);");			
			ResultSet set = dbConnector.executeSelect(
					"select max(id_image) from ps_image;");			
			if (set.next()){			
					picId = set.getInt(1);			
				} else {
					System.out.println("No id_image returned");
				}
		} catch (SQLException e) {
			System.out.println("Max id not selected");
			e.printStackTrace();
		}
		pictureId = String.valueOf(picId);
		dbConnector.execute("insert into ps_image_shop values(" + pictureId + ", " + shopId + ", 1)");
		dbConnector.execute("insert into ps_image_lang values(" + pictureId + ", " + languageId + ","
				+ "\"" + productName + "\");");
		return pictureId;
	}

}
