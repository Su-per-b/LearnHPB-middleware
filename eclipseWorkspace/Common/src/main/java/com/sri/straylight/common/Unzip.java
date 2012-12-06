package com.sri.straylight.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Unzip {
	
	
	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
	}
	

	public static final void unzip(String filename, String unZipFolder) {
		
//		Enumeration<ZipEntry> entries;
		
		Enumeration<? extends ZipEntry> entries;
		
		ZipFile zipFile;

		//make directories
		try {
			

			zipFile = new ZipFile(filename);
			entries = zipFile.entries();
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String entryName = entry.getName();
				entryName = entryName.replace("/", File.separator);
				
				if (entry.isDirectory()) {
					System.out.println("Extracting directory: " + entryName);
					(new File(unZipFolder + File.separator + entryName)).mkdirs();
				}
				
			}
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
		
		//make files
		try {

			entries =  zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String entryName = entry.getName();
				entryName = entryName.replace("/", File.separator);
				
				
				String outputFilePath = unZipFolder + File.separator + entryName;
				
				makeSureFolderExists(outputFilePath);
				//File f = new File(outputFilePath);
				//f.mkdirs();
				
				
				
				if (!entry.isDirectory()) {
					System.out.println("Extracting file: " + entryName);
					
					
					InputStream inputStream = zipFile.getInputStream(entry);
					
					
					
					FileOutputStream outputFileStream = new FileOutputStream(outputFilePath);
					BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputFileStream);
					
					copyInputStream(
							inputStream,
							bufferedOutputStream
							);
				}
				
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
		
		
	}
	
	public static final void makeSureFolderExists(String filePath) {

		
		String[] subDirs = filePath.split(Pattern.quote(File.separator));
		String folder = subDirs[0];
		
		for (int i = 1; i < subDirs.length-1; i++) {
			folder += File.separator + subDirs[i];
		}
		
		File f = new File(folder);
		f.mkdirs();

	}
	
	
}