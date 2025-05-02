package com.bb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileWriteUtil {
	
	
	/**
	 * 파일 쓰기
	 * 
	 * @param filePath
	 * @param stringList
	 * @param bAppend
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static boolean writeFile(String filePath, ArrayList<String> stringList, boolean bAppend) throws IOException, Exception {
		if (filePath == null || filePath.length() == 0) {
			return false;
		}
		
		File file = new File(filePath);
		
		boolean bWrite = false;
		
		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bufferedWriter = null;
		
		try {
			fileOutputStream = new FileOutputStream(file, bAppend);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			bufferedWriter = new BufferedWriter(outputStreamWriter);
		
			if (stringList != null && stringList.size() > 0) {
				String oneLine = null;
				
				int lineCount = stringList.size();
				int lastIndex = lineCount - 1;
				
				for (int i=0; i<lineCount; i++) {
					oneLine = stringList.get(i);
					
					bufferedWriter.write(oneLine, 0, oneLine.length());
//					if (i < lastIndex) {
//						bufferedWriter.write(delimiter);
//						// bufferedWriter.newLine();
//					}
				}
			}
			
			bWrite = true;
			
		} catch (IOException e) {
			throw e;
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (Exception e) {
				// 무시
			}
			
			try {
				if (outputStreamWriter != null) {
					outputStreamWriter.close();
				}
			} catch (Exception e) {
				// 무시
			}
			
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (Exception e) {
				// 무시
			}
		}
		
		return bWrite;
	}
}
