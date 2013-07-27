package prestashop.img;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class ImgImporter  {
	
	private File localSrcDir;
	private File localDestDir;
	private ImgDbHelper dbHelp;
	
	public ImgImporter(String src, String dest, String user, String passwd, String db) {
		this.localSrcDir = new File(src);
		this.localDestDir = new File(dest);
		this.dbHelp = new ImgDbHelper(user, passwd, db);
	}
	
	private boolean validateDestDir(File destDir) {
		return (destDir.isDirectory());
	}
	
	private boolean validateSrcDir(File srcDir) {
		return (srcDir.isDirectory() && srcDir.listFiles().length > 0);
	}
	
	private boolean initDirectories() {
		boolean result =  validateDestDir(this.localDestDir) && validateSrcDir(this.localSrcDir);
		if (!result)
			System.out.println("Directories are not valid. Exiting.");
		return result;		
	}
	
	
	public void run() {
		if (!initDirectories()) {
			try {
				System.out.println("Press any key to finish.");
				System.in.read();
			} catch (IOException e) {
				return;
			}
			return;
		}
		dbHelp.open();
        processFiles(this.localSrcDir);
        dbHelp.close();
	}
	
	private String extractProductName(File img){
		String name = img.getName();
		return name.substring(0, name.lastIndexOf('.'));
	}
	
	private String getPath(String pictureId) {
		char[] numbers = pictureId.toCharArray();
		StringBuffer newDir = new StringBuffer(this.localDestDir.getPath());
		for (char c : numbers) {
			newDir.append('\\').append(c);			
			}
		return newDir.toString();
	}
	
	private boolean prepareDirectory(String pictureId) {
		String newPath = this.getPath(pictureId);		
		try {
			Files.createDirectories(Paths.get(newPath));
		} catch (IOException e) {
			System.out.println("Creating dirs failed");
			e.printStackTrace();
			return false;
		}
		System.out.println("Picture id is " + pictureId + "and dest dir is " + newPath);
		return true;
	}
	
	public void processFile(File img) {
		String productName = extractProductName(img);
		String productId = dbHelp.getDbProductId(productName);
		String existingImageId = dbHelp.getImgIdForDbProductName(productName);
		// There is no product in the database, file omitted
		if (productId == null) {
			System.out.println("Product " + productName + " ignored.");
			return;
		// Product is in the database 
		} else {
			// product does not have any image
			if (existingImageId == null) {
				System.out.println("Inserting the picture to database: " + img);
				existingImageId = dbHelp.insertPicture(productId, productName);	
			}
			// prepare the directory for the image
			prepareDirectory(existingImageId);
			try {
				// copy and rename the image
				Files.copy(Paths.get(img.getPath()), Paths.get(getPath(existingImageId) + "/" + existingImageId + ".jpg"), StandardCopyOption.valueOf("REPLACE_EXISTING"));
			} catch (IOException e) {
				System.out.println("Failure copying file");
				e.printStackTrace();
			}
			
		}
	}
	
	public void processFiles(File dir){
		File[] files = dir.listFiles();
		for (File f : files) {
			if (f.isDirectory())
				processFiles(f);
			else
				processFile(f);
		}
	}
	
	public static void main(String args[]) {
		System.out.println("Execution started!");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String user = "user";
		String passwd = "password"; 
		String db = "db address";
		String srcimg = "sourcefoleder";
		String destimg = "destination folder";		
		try {		
		if (args.length > 0)
			user = args[0];
		else {
			System.out.println("Database login: ");
			user = br.readLine();
		}
		if (args.length > 1)
			passwd = args[1];
		else {
			System.out.println("Database password: ");
			passwd = br.readLine();
		}		
		if (args.length > 2)
			db = args[2];
		else {
			System.out.println("Database address: ");
			db = br.readLine();
		}		
		if (args.length > 3)
			srcimg = args[3];
		else {
			System.out.println("Source folder with pictures: ");
			srcimg = br.readLine();
		}
		
		if (args.length > 4)
			destimg = args[4];
		else {
			System.out.println("Destination folder for creation of images hierarchy: ");
			destimg = br.readLine();
		}
		} catch (Exception e) {
			System.out.println("Exception while reading from database");
		}
		
		new ImgImporter(srcimg, destimg, user, passwd, db).run();
	}

}
