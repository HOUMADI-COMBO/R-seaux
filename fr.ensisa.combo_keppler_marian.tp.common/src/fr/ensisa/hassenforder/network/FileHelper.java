package fr.ensisa.hassenforder.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

	public static String [] getDirectory () {
		File directory = new File (".");
		String [] content = directory.list();
		return content;
	}

	public static long getFileSize (String filename) {
		File localFile = new File (filename);
		long length = -1;
		if (localFile.exists()) {
			length = localFile.length();
		}
		return length;
	}

	public static byte [] readContent (String filename) {
		if (filename == null) return null;
		byte [] content = null;
		try {
			File localFile = new File (filename);
			if (! localFile.exists()) return null;
			long length = localFile.length();
			if (length != 0) {
				content = new byte [(int) length];
				FileInputStream fis = new FileInputStream (localFile);
				BufferedInputStream bis = new BufferedInputStream (fis);
				bis.read(content);
				bis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void writeContent (String filename, byte [] content) {
		if (content == null) return ;
		try {
			File localFile = new File (filename);
			FileOutputStream fos = new FileOutputStream (localFile);
			BufferedOutputStream bos = new BufferedOutputStream (fos);
			bos.write(content);
			bos.close();
		} catch (IOException e) {
		}
	}

	public static boolean fileExists(String filename) {
		File localFile = new File (filename);
		return localFile.exists();
	}
}
