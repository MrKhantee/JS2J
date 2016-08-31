package js2j.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class FileUtils {
	
	public static void createFile(File file, Object content) {
		FileOutputStream fop = null;
		try { 
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = ((String) content).getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createFile(String filePath, String content) {
		createFile(new File(filePath),content);
	}
	
	public static String readTemporyFile(String filePath) {
		BufferedReader br = null;
		String sCurrentLine, content = "";
		try {
 			br = new BufferedReader(new FileReader(filePath));
 
			while ((sCurrentLine = br.readLine()) != null)
				content += sCurrentLine;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
//		deleteFile(filePath);
		return content;
	}
	
	public static void deleteFile(String filePath) {
		new File(filePath).delete();
	}
	
    public static String getFileCheckSum(String filePath) {
        byte[] mdbytes = filePath.getBytes();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    
	public static Process compile(String cp, File file) {
		try {
			String command = "javac -cp "+cp+" "+file.getAbsolutePath();
			System.out.println("Command:"+command);
			return Runtime.getRuntime().exec(command);
		} catch (IOException r) {
			r.printStackTrace();
		}
		return null;
	}
	
	public static Process buildJarFile(String outputDirectory, String[] filesNames) {
		try {
			String command = "jar cfv "+outputDirectory+File.separator+new Date().getTime()+".jar "+StringUtils.join(filesNames,' ');
			System.out.println("Command:"+command);
			return Runtime.getRuntime().exec(command);
		} catch (IOException r) {
			r.printStackTrace();
		}
		return null;
	}
	
	public static Process run(File file) {
		try {
			String programName = file.getName().substring(0, file.getName().indexOf('.'));
			String command = "java -cp "+file.getParent()+" "+programName;
			return Runtime.getRuntime().exec(command);
		} catch (IOException r) {
			r.printStackTrace();
		}
		return null;
	}
	
	public static String js2jFilePath = "C:\\Users\\Max\\Desktop\\js2j";
	public static String utilsFilePath = js2jFilePath+"\\utils";
	public static String buildFilePath = js2jFilePath+"\\build";
	public static String checkSumFilePath = utilsFilePath+"\\checksum";
	
	public static void buildCompile(File file, String code) {
		FileUtils.createFile(file, code);

		String jsRunnableFunctionFileCheckSum = FileUtils.getFileCheckSum(file.getAbsolutePath());
		File jsRunnableFunctionFileChecksumFile = new File(checkSumFilePath+File.separator+jsRunnableFunctionFileCheckSum);
		File jsRunnableFunctionFileByteCodeFile = new File(utilsFilePath+File.separator+FileUtils.getFileNameWithoutExtension(file)+".class");
		try {
			if(!jsRunnableFunctionFileChecksumFile.exists()
					|| !jsRunnableFunctionFileByteCodeFile.exists()) {
				jsRunnableFunctionFileChecksumFile.createNewFile();
				System.out.println("Waiting compiling "+ file.getName() +"...");
				Process c = FileUtils.compile(js2jFilePath,file);
				c.waitFor();
			} else {
				System.out.println("Compiling "+ file.getName() +" passed!");
			}
//		    System.out.println("Waiting running "+ jsRunnableFunctionFile.getName() +"...");
//			Process r = FileUtils.run(jsRunnableFunctionFile);
//			r.waitFor();
		    System.out.println("Done.");
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void buildProject() {
		try {
			System.out.println("Waiting building  "+ new Date().getTime() +" project as jar file...");
			String[] filesPaths = listFilesWithGivenExtension(utilsFilePath, "class");
			Process c = FileUtils.buildJarFile(buildFilePath, filesPaths);
			c.waitFor();
		    System.out.println("Done.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getFileNameWithoutExtension(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    name = name.substring(0, pos);
		}
		return name;
	}
	
	public static String[] listFilesWithGivenExtension(String folder, String ext) { 
		GenericExtensionFilter filter = new GenericExtensionFilter(ext);
		File dir = new File(folder);
 
		if(dir.isDirectory()==false){
			System.out.println("Directory does not exists : " + dir);
			return null;
		}
 
		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);
		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return null;
		}
		
		ArrayList<String> filesPaths = new ArrayList<String>();
		
		for (String file : list) {
			String temp = new StringBuffer("-C "+dir.getParentFile().getAbsolutePath())
					.append(" utils/"+file).toString();
			filesPaths.add(temp);
		}
		// Add dependencies
//		String dependencies = new StringBuffer("-C "+dir.getParentFile().getAbsolutePath())
//			.append(" dependencies/json-simple-1.1.1.jar").toString();
//		filesPaths.add(dependencies);
		String[] filesPathsArray = new String[filesPaths.size()];
		return filesPaths.toArray(filesPathsArray);
	}
}