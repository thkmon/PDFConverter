package com.bb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FileReadUtil {

	
	/**
	 * 파일 읽기
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static ArrayList<String> readFile(String filePath) throws IOException, Exception {
		if (filePath == null || filePath.length() == 0) {
			return null;
		}
		
		return readFile(new File(filePath));
	}
	
	
	/**
	 * 파일 읽기
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static ArrayList<String> readFile(File file) throws IOException, Exception {
		if (file == null || !file.exists()) {
			return null;
		}
		
		ArrayList<String> resultList = null;
		
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
		
			String oneLine = null;
			while ((oneLine = bufferedReader.readLine()) != null) {
				if (resultList == null) {
					resultList = new ArrayList<String>();
				}
				
				resultList.add(oneLine);
			}

		} catch (IOException e) {
			throw e;
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				// 무시
			}
			
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				// 무시
			}
			
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				// 무시
			}
		}
		
		return resultList;
	}
	
	
	/**
	 * properties 파일 읽기
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static HashMap<String, String> readPropertiesFile(String filePath) throws IOException, Exception {
		if (filePath == null || filePath.length() == 0) {
			return null;
		}
		
		ArrayList<String> propertiesContent = readFile(filePath);
		if (propertiesContent == null || propertiesContent.size() == 0) {
			return null;
		}
		
		HashMap<String, String> resultMap = null;
		
		int equalIndex = 0;
		String leftStr = null;
		String rightStr = null;
		
		String oneLine = null;
		int lineCount = propertiesContent.size();
		for (int i=0; i<lineCount; i++) {
			oneLine = propertiesContent.get(i);
			if (oneLine.trim().length() == 0) {
				continue;
			}
			
			// 주석 무시
			if (oneLine.trim().startsWith("#")) {
				continue;
			}
			
			// 등호 없으면 무시
			equalIndex = oneLine.indexOf("=");
			if (equalIndex < 0) {
				continue;
			}
			
			leftStr = oneLine.substring(0, equalIndex);
			rightStr = oneLine.substring(equalIndex + 1);
			
			if (leftStr.trim().length() > 0) {
				if (resultMap == null) {
					resultMap = new HashMap<String, String>();
				}
				
				resultMap.put(leftStr.trim(), rightStr.trim());
			}
		}
		
		return resultMap;
	}
}