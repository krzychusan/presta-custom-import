package prestashop.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prestashop.utils.DATA_TYPE;

class DescriptionDecorator {
	private String pattern;
	private String description;

	public DescriptionDecorator(String p, String d) {
		this.pattern = p;
		this.description = d;
	}

	public String getDescription() {
		return description;
	}
	
	public boolean canMatch(String word) {
		System.out.println("Checking " + word + " against [" + this.pattern + "]");
		Pattern p = Pattern.compile(this.pattern);
		Matcher m = p.matcher(word);
		System.out.println(m.matches());
		return m.matches();
	}

}

public class PatternFileMapCreator {
	
	public static void main(String args[]) {
		PatternFileMapCreator pf = new PatternFileMapCreator(new File("D:/ate_nowe_zdjecia/klocki_opisy"),
				new File("D:/ate_nowe_zdjecia/tarcze_opisy"));
		
		for (DescriptionDecorator d : pf.padsList) {
			System.out.println(d.getDescription().substring(0,100));
		}
		
		Pattern p = Pattern.compile("..\\...7.-....\\..");
		Matcher m = p.matcher("13.0470-5954.2");
		System.out.println(m.matches());
		System.out.println("MATCH FOUND!: " + pf.getMatchingDescription(DATA_TYPE.BRAKEPADS_BACK, "13.0470-5954.2"));
		
	}

	private ArrayList<DescriptionDecorator> padsList = new ArrayList<>();
	private ArrayList<DescriptionDecorator> shieldsList = new ArrayList<>();

	private DescriptionDecorator parseFile(File f) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		String firstLine = null;
		StringBuffer description = new StringBuffer("");
		try {
			firstLine = br.readLine();
			String line = null;
			while ((line = br.readLine()) != null) {
				description.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new DescriptionDecorator(firstLine, description.toString());
	}

	private ArrayList<DescriptionDecorator> createMap(File src) {
		ArrayList<DescriptionDecorator> list = new ArrayList<>();
		File[] files = src.listFiles();
		for (int i = 0; i < files.length; ++i) {
			list.add(parseFile(files[i]));
		}
		return list;
	}
	
	public PatternFileMapCreator(File padsDesc, File shieldsDesc) {
		this.createPadsMap(padsDesc);
		this.createShieldsMap(shieldsDesc);
	}
 
	private void createPadsMap(File padsDescriptionDir) {
		this.padsList = createMap(padsDescriptionDir);
	}

	private void createShieldsMap(File shieldsDescriptionDir) {
		this.shieldsList = createMap(shieldsDescriptionDir);
	}
	
	private String getMatchingDesc(String name, ArrayList<DescriptionDecorator> dd) {		
		for (DescriptionDecorator d : dd) {
			if (d.canMatch(name)) {
				System.out.println("Match found.");
				return d.getDescription();
			}
		}
		return null;
	}

	public String getMatchingDescription(DATA_TYPE type, String name) {
		if (type.equals(DATA_TYPE.BRAKEPADS_BACK) || type.equals(DATA_TYPE.BRAKEPADS_FRONT)) {
			return getMatchingDesc(name, padsList);
		} else if (type == DATA_TYPE.SHIELDS_BACK || type == DATA_TYPE.SHIELDS_FRONT) {
			return getMatchingDesc(name, shieldsList);
		}
		return null;	
	}

}
