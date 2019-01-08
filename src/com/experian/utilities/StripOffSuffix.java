package com.experian.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StripOffSuffix {
	static long delay_time = 1000;
	static String default_path = "";
	static long loop_time = 1;

	public static void main(String args[]) throws Exception {
		

		if (args.length == 0) {
			System.out.println("Use current path");
			System.out.println("Args 0: delay time");
			System.out.println("Args 1: loop time");
			System.out.println("Args 2: default path");
			System.exit(0);
		}
		else {
			System.out.println("Argument 0 : " + args[0]);
			System.out.println("Argument 1 : " + args[1]);
			System.out.println("Argument 2 : " + args[2]);
			
			if (args[0] != null && args[0].trim().length() > 0)
				delay_time = Long.parseLong(args[0]);
			
			if(args[1] != null && args[1].trim().length() > 0) 
				loop_time = Long.parseLong(args[1]); 

			if(args[2] != null && args[2].trim().length() > 0) 
				default_path = args[2]+"\\"; 
		}

		int a=0;
		while(a<loop_time) {
			System.out.println("Processing, scanning folder... loop : " + (a+1));
			Thread.sleep(delay_time);
			runFilenameCheckingAndTransform();
			a++;
		}
	}

	private static void runFilenameCheckingAndTransform() throws IOException {
		//TODO: Scan current folder
		String currentDefaultFolder = default_path.trim().length() > 0 ? default_path : "\\.";
		
		ArrayList<String> filesToChanges = getAllFilesInCurrentFolder();

		//TODO: Detect file, move if after strip there is existing file
		for (int a=0;a<filesToChanges.size();a++) {
			System.out.print("Processing... " + filesToChanges.get(a).toString());
			String name = filesToChanges.get(a).split("\\.")[0];
			String extension = filesToChanges.get(a).split("\\.")[1];

			//TODO: Rename
			String new_name = stripOffDetectNumber(name) +"."+ extension;
			System.out.println(" rename to " + new_name);
			File newFile = new File(currentDefaultFolder+new_name);
			File oldFile = new File(currentDefaultFolder+filesToChanges.get(a));
			
			if(!oldFile.exists()) {
				System.out.println(oldFile.getPath() + " Missing...");
				continue;
			}
			
			/*if(newFile.exists())
				Files.move(Paths.get(currentDefaultFolder+newFile.getName()), Paths.get(currentDefaultFolder+"archive\\"+newFile.getName()), StandardCopyOption.REPLACE_EXISTING);*/

			if(oldFile.renameTo(newFile)){
				System.out.println("Success !");
			}
			else  {
				if (newFile.exists())
					System.out.println("Failed ! - Targeted new file name is already exist !");
				else
					System.out.println("Failed ! - For anything... check everything");
			}
		}


	}

	private static ArrayList<String> getAllFilesInCurrentFolder() {
		ArrayList<String> result = new ArrayList<String>();

		File folder = new File("C:\\Users\\C19962A\\Documents\\Custom Office Templates");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				result.add(listOfFiles[i].getName());
				//				System.out.println("File " + listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				//				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		return result;
	}

	private static int lengthIndexNumberStarted (String value) {
		for (int a=0; a<value.length(); a++) {
			if (Character.isDigit(value.charAt(a)))
			{
				if((a > 0) && (Character.isDigit(value.charAt(a)) && (value.charAt(a-1) == '_')))
					return a-1;
				else
					return a;
			}
		}
		return value.length();
	}

	private static String stripOffDetectNumber (String value) {
		String result = value.substring(0, lengthIndexNumberStarted(value));
		return result.trim();
	}

	public String stripOff(String value, String fullpattern, String patterntoremove) {
		int start_index_to_be_removed = getIndexStartedToStrip(fullpattern, patterntoremove);

		String result = value.substring(0, start_index_to_be_removed);
		return result;
	}

	public int getIndexStartedToStrip (String fullpattern, String patterntoremove) {
		int index = fullpattern.indexOf(patterntoremove);
		return index;
	}
}
