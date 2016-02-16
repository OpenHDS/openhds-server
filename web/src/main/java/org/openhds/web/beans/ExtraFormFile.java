package org.openhds.web.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtraFormFile extends File {

	public ExtraFormFile(String parent, String child) {
		super(parent, child);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 9189385622812257957L;
	
	public long getLastModified(){
		return super.lastModified();
	}
	
	public Date getLastModifiedDate(){
		return new Date(super.lastModified());
	}
	
	public String getLastModifiedDateFormatted(){
		Date d = new Date(super.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return sdf.format(d);
	}
	
	public String getLengthFormatted(){
		return readableFileSize(super.length());
	}
	
	public int getLineCount(){
		int countLines = countLines();
		return countLines;
	}
	
	public int countLines(){
		int count = 0;
		FileInputStream stream;
		try {
			stream = new FileInputStream(this);
			byte[] buffer = new byte[8192];
			int n;
			while ((n = stream.read(buffer)) > 0) {
			    for (int i = 0; i < n; i++) {
			    	if (buffer[i] == '\n') count++;
			    }
			}
			stream.close();
		} catch (FileNotFoundException e) {} 
		catch (IOException e) {}
		return count;
	}
	
	private String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
