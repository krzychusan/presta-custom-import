package prestashop.img;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

enum FileNamePattern {
	STARTS_WITH_60,
	STARTS_WITH_ID,
	STARTS_WITH_4,
	STARTS_WITH_5;
	
	public boolean matchesName(String name) {
		System.out.println("Name: " + name);
		if (this.equals(STARTS_WITH_60))
			return name.startsWith("60");
		else if (this.equals(STARTS_WITH_ID)) {
			System.out.println("Checking if the name matches id pattern, will return " + name.startsWith("ld"));
			
			return name.startsWith("ld");
		}
		else if (this.equals(STARTS_WITH_5))
			return name.startsWith("5");
		else if (this.equals(STARTS_WITH_4))
			return name.startsWith("4");
		return false;
	}
	
	public static FileNamePattern forString(String temp) {
		if (temp.startsWith("60"))
			return STARTS_WITH_60;
		else if (temp.startsWith("ld")) {
			System.out.println("Starts with i returned");
			return STARTS_WITH_ID;
		}
		else if (temp.startsWith("4"))
			return STARTS_WITH_4;
		else if (temp.startsWith("5"))
			return STARTS_WITH_5;
		else return null;
	}
}

public class ImgRenamer implements Runnable {
	
	private FileNamePattern pattern;
	private File srcFolder;
	private File dstFolder;
	
	
	public ImgRenamer(FileNamePattern pattern, File srcFolder, File dstFolder) {
		this.pattern = pattern;
		this.srcFolder = srcFolder;
		this.dstFolder = dstFolder;
	}
	
	public static void main(String args[]) {
		if (args.length != 3)
			System.out.println("The arguments must be: the pattern to use (4,5,60,ld), source folder, destination folder");
		//new ImgRenamer(FileNamePattern.STARTS_WITH_60, new File("D:/ate_nowe_zdjecia/Ate_nowe zdjecia/Ate_klocki_p_st"), new File("D:/ate_nowe_zdjecia/result")).run();			
		new ImgRenamer(FileNamePattern.forString(args[0]),
					   new File(args[1]),
					   new File(args[2])).run();			

	}
	
	private String newNameFor(String name) {
		if (!pattern.matchesName(name))
			return name;
		String newName = null;
		int lastDot = name.lastIndexOf(".");
		if (pattern == FileNamePattern.STARTS_WITH_60) {			
		    newName = "13.04" + name.substring(0,2) + "-" + name.substring(2,lastDot) + ".2" + name.substring(lastDot);
			//602829 -> 13.0460-2829.2
		} else if (pattern == FileNamePattern.STARTS_WITH_5) {
			// 509114 -> 24.0309-0114.1
			newName = "24.03" + name.substring(1,3) + "-0" + name.substring(3,6) + ".1" + name.substring(lastDot);
		} else if (pattern == FileNamePattern.STARTS_WITH_4) {
			// 408100 - > 24.0108-0100.1		}
			newName = "24.01" + name.substring(1,3) + "-0" + name.substring(3,6) + ".1" + name.substring(lastDot);
		} else if (pattern == FileNamePattern.STARTS_WITH_ID) {
			 //ld2708 -> 13.0470-2708.2
			newName = "13.0470-" + name.substring(2,lastDot) + ".2" + name.substring(lastDot);
		}
		return newName;
		
	}
	
	private boolean copyAndRename(File f) {
		String newName = newNameFor(f.getName());
		try {
			Files.copy(Paths.get(f.getPath()), Paths.get(dstFolder.getPath() + "/" +  newName), StandardCopyOption.valueOf("REPLACE_EXISTING"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean processFiles(File dir) {
		File[] files = dir.listFiles();
		boolean result = true;
		for (int i =0; i < files.length; ++i) {
			if (files[i].isDirectory())
				processFiles(files[i]);
			else 
				result = result && copyAndRename(files[i]);
		}
		return result;
	}

	@Override
	public void run() {
		processFiles(srcFolder);		
	}

}
