package com.thkmon.pdfconv.util;

import java.io.File;

import com.thkmon.pdfconv.prototype.FilePathList;

public class FilePathUtil {

	/**
	 * 패스를 밸리드하게 보정한다.
	 * 
	 * @param path
	 * @return
	 */
	public static String revisePathToValid(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}

		// 역슬래시는 슬래시로 변경
		path = path.replace("\\", "/");

		// 연속된 슬래시는 1개로 변경
		while (path.indexOf("//") > -1) {
			path = path.replace("//", "/");
		}

		return path;
	}

	/**
	 * 파일을 만든다. 1. 파일이 존재하지 않을 경우 새로 만든다. 2. 파일이 존재할 경우 넘어간다. (boolean
	 * removeExistingFile 값에 따라 삭제하고 새로 만든다.) 3. 파일이 존재하지 않을 경우 상위 폴더 트리 및 파일을 만든다.
	 * 
	 * @param filePath
	 * @param removeIfExists
	 * @return
	 */
	public static boolean makeFile(String filePath, boolean removeExistingFile) {
		if (filePath == null || filePath.length() == 0) {
			return false;
		}

		filePath = revisePathToValid(filePath);

		boolean result = false;

		try {
			File file = new File(filePath);

			// 파일이 존재할 경우
			if (file.exists()) {
				if (!file.isFile()) {
					System.err.println("해당 경로는 파일이 아닌 디렉토리임. makeFile 처리 불가 : " + file.getAbsolutePath());
					return false;
				}

				if (removeExistingFile) {
					file.delete();
					file.createNewFile();
				}
				return true;
			}

			// 파일이 존재하지 않을 경우
			makeParentDirs(filePath);
			file.createNewFile();

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 특정 파일패스의 부모 폴더가 없을 경우 만든다.
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean makeParentDirs(String filePath) {

		if (filePath == null || filePath.trim().length() == 0) {
			System.err.println("makeParentDirs : filePath == null || filePath.length() == 0");
			return false;

		} else {
			filePath = filePath.trim();
		}

		// 역슬래시는 슬래시로 변경
		filePath = filePath.replace("\\", "/");

		// 연속된 슬래시는 1개로 변경
		while (filePath.indexOf("//") > -1) {
			filePath = filePath.replace("//", "/");
		}

		// 필요한 디렉토리 만들기
		int lastSlashPos = filePath.lastIndexOf("\\");

		if (lastSlashPos > -1) {
			File d = new File(filePath.substring(0, lastSlashPos));
			if (!d.exists()) {
				d.mkdirs();
			}

		} else {
			System.err.println("makeParentDirs : lastSlashPos not exists");
			return false;
		}

		return true;
	}

	/**
	 * 자바 파일만 가져오기
	 * 
	 * @param rootPath
	 * @param exceptPattern
	 * @return
	 */
	public static FilePathList getJavaPathList(String rootPath, String exceptPattern) {
		String includePattern = "*.txt";

		FilePathList pathList = makePathList(rootPath, includePattern, exceptPattern);

		if (pathList == null || pathList.size() == 0) {
			return null;
		}

		return pathList;
	}

	public static FilePathList getJspPathList(String rootPath, String exceptPattern) {
		String includePattern = "*.jsp";

		FilePathList pathList = makePathList(rootPath, includePattern, exceptPattern);

		if (pathList == null || pathList.size() == 0) {
			return null;
		}

		return pathList;
	}

	public static FilePathList getJsPathList(String rootPath, String exceptPattern) {
		String includePattern = "*.js";

		FilePathList pathList = makePathList(rootPath, includePattern, exceptPattern);

		if (pathList == null || pathList.size() == 0) {
			return null;
		}

		return pathList;
	}

	public static FilePathList getPathList(String path, String includePattern, String exceptPattern) {

		FilePathList pathList = makePathList(path, includePattern, exceptPattern);

		if (pathList == null || pathList.size() == 0) {
			return null;
		}

		return pathList;
	}

	/**
	 * 표준 패스 가져오기
	 * 
	 * @param rootPath
	 * @return
	 */
	public static FilePathList getStandardPathList(String rootPath) {
		String includePattern = "*.js,*.jsp,*.htm,*.html,*.java,*.config,*.properties,*.xml";
		String exceptPattern = "";

		FilePathList pathList = makePathList(rootPath, includePattern, exceptPattern);

		if (pathList == null || pathList.size() == 0) {
			return null;
		}

		return pathList;
	}

	/**
	 * path와 제외할 폴더명들, 제외할 파일명들을 넘기면 어레이리스트로 파일 패스 목록을 리턴하는 메서드.
	 * 
	 * @param path
	 * @param exceptFolders
	 * @param exceptFile
	 * @return
	 */
	public static FilePathList makePathList(String path, String includePattern, String exceptPattern) {
		FilePathList pathList = new FilePathList();
		try {
			if (path == null || path.length() < 0) {
				System.err.println("패스가 없습니다");
			}

			if (includePattern == null) {
				includePattern = "";
			}

			if (exceptPattern == null) {
				exceptPattern = "";
			}

			// 역슬래시는 슬래시로 변경
			includePattern = includePattern.replace("\\", "/");

			// 쌍슬래시를 슬래시 한 개로.
			while (includePattern.indexOf("//") > -1) {
				includePattern = includePattern.replace("//", "/");
			}

			includePattern = includePattern.replace("\\", "\\\\"); // 역슬래시를 정규식 역슬래시로
			includePattern = includePattern.replace(".", "\\."); // 점은 정규식 점으로
			includePattern = includePattern.replace("*", ".*"); // 별은 정규식 모든문자로
			includePattern = includePattern.replace("?", "."); // 물음표는 정규식 한문자로

			// 역슬래시는 슬래시로 변경
			exceptPattern = exceptPattern.replace("\\", "/");

			// 쌍슬래시를 슬래시 한 개로.
			while (exceptPattern.indexOf("//") > -1) {
				exceptPattern = exceptPattern.replace("//", "/");
			}

			exceptPattern = exceptPattern.replace("\\", "\\\\"); // 역슬래시를 정규식 역슬래시로
			exceptPattern = exceptPattern.replace(".", "\\.");
			exceptPattern = exceptPattern.replace("*", ".*");
			exceptPattern = exceptPattern.replace("?", ".");

			String[] includeArr = includePattern.split(",");
			String[] exceptArr = exceptPattern.split(",");

			path = path.replace("\\", "/");

			while (path.indexOf("//") > -1) {
				path = path.replace("//", "/");
			}

			File root = new File(path);

			if (!root.exists()) {
				System.err.println("해당 패스가 존재하지 않습니다. : " + root.getAbsolutePath());
				return null;
			}

			if (root.isDirectory()) {
				addPath(root, pathList, includeArr, exceptArr);
			} else if (root.isFile()) {
				pathList.add(root.getAbsolutePath());
				return pathList;

			} else {
				System.err.println("해당 패스가 파일인지 폴더인지 알 수 없습니다 : " + root.getAbsolutePath());
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return pathList;
	}

	private static void addPath(File dir, FilePathList pathList, String[] includeArr, String[] exceptArr) {
		if (dir.isDirectory()) {

			String[] dirList = dir.list();
			int len = dirList.length;
			for (int i = 0; i < len; i++) {
				String fullPath = dir.getAbsolutePath() + "\\" + dirList[i];

				File file = new File(fullPath);
				addPath(file, pathList, includeArr, exceptArr);
			}

		} else if (dir.isFile()) {
			String filePath = dir.getAbsolutePath();

			boolean include = false;
			int len = includeArr.length;
			for (int k = 0; k < len; k++) {

				if (filePath.matches(includeArr[k])) {
					// 매치가 되면 break;
					include = true;
					break;
				}
			}

			if (!include) {
				return;
			}

			len = exceptArr.length;
			for (int k = 0; k < len; k++) {
				if (filePath.matches(exceptArr[k])) {
					return;
				}
			}

			pathList.add(filePath);
		}
	}

	/**
	 * filePath 의 특정 파일이 존재하는지 검사하여 리스트에 실어 리턴한다. 파일이 존재하지 않을 경우 rootDir 안쪽을 파일명으로
	 * 검색해서 찾아낸다. 동일한 파일명으로 파일을 찾을 수 없다면 null 또는 빈 리스트를 리턴한다.
	 * 
	 * @param filePath
	 * @param rootDirPath
	 * @return
	 */
	public static FilePathList getExistingFileList(String filePath, String rootDirPath) {
		if (filePath == null || filePath.length() == 0) {
			return null;
		}

		FilePathList filePathList = new FilePathList();
		filePathList.add(filePath);

		return getExistingFileList(filePathList, rootDirPath);
	}

	/**
	 * filePathList 내 특정 파일이 존재하는지 검사하여 리스트에 실어 리턴한다. 파일이 존재하지 않을 경우 rootDir 안쪽을
	 * 파일명으로 검색해서 찾아낸다. 동일한 파일명으로 파일을 찾을 수 없다면 null 또는 빈 리스트를 리턴한다.
	 * 
	 * @param filePathList
	 * @param rootDirPath
	 * @return
	 */
	public static FilePathList getExistingFileList(FilePathList filePathList, String rootDirPath) {
		if (filePathList == null || filePathList.size() == 0) {
			return null;
		}

		FilePathList resultPathList = new FilePathList();

		String fileNameAndExtension = null;
		File oneFile = null;
		String onePath = null;
		int count = filePathList.size();

		for (int i = 0; i < count; i++) {
			onePath = filePathList.get(i);
			if (onePath == null || onePath.length() == 0) {
				continue;
			}

			// 밸리드한 파일명인지 확인
			int idxDot = onePath.lastIndexOf(".");
			if (idxDot < 0 || !onePath.substring(idxDot + 1).matches("[a-zA-Z]*")) {
				continue;
			}

			oneFile = new File(onePath);
			if (oneFile.exists()) {
				resultPathList.add(oneFile.getAbsolutePath());
				continue;
			}

			onePath = revisePathToValid(rootDirPath + "\\" + onePath);
			oneFile = new File(onePath);
			if (oneFile.exists()) {
				resultPathList.add(oneFile.getAbsolutePath());
				continue;
			}

			fileNameAndExtension = getFileNameAndExtension(onePath);
			FilePathList newList = getFileListBySearch(rootDirPath, fileNameAndExtension);
			if (newList != null && newList.size() > 0) {
				resultPathList.appendList(newList);
			}
		}

		return resultPathList;
	}

	/**
	 * 특정 폴더 하위에서 특정 파일명과 일치하는 리스트를 가져온다.
	 */
	public static FilePathList getFileListBySearch(String targetDirPath, String fileNameAndExtension) {
		if (fileNameAndExtension == null || fileNameAndExtension.length() == 0) {
			System.err.println("fileNameAndExtension == null || fileNameAndExtension.length() == 0");
			return null;
		}

		return makePathList(targetDirPath, "*/" + fileNameAndExtension, "");
	}

	/**
	 * 파일 패스를 넘기면 파일명과 확장자를 얻는다.
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameAndExtension(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return "";
		}

		// 역슬래시는 슬래시로 변경
		filePath = filePath.replace("\\", "/");

		// 연속된 슬래시는 1개로 변경
		while (filePath.indexOf("//") > -1) {
			filePath = filePath.replace("//", "/");
		}

		int idxLastSlash = filePath.lastIndexOf("/");
		if (idxLastSlash > -1) {
			filePath = filePath.substring(idxLastSlash + 1);
		}

		return filePath;
	}
	
	/**
	 * @param filePath
	 * @return
	 */
	public static String getDirPath(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return "";
		}
		
		// 역슬래시는 슬래시로 변경
		filePath = filePath.replace("\\", "/");
		
		// 연속된 슬래시는 1개로 변경
		while (filePath.indexOf("//") > -1) {
			filePath = filePath.replace("//", "/");
		}
		
		int idxLastDot = filePath.lastIndexOf("/");
		if (idxLastDot > -1) {
			return filePath.substring(0, idxLastDot);
		}
		return filePath;
	}
	
	/**
	 * 파일 패스를 넘기면 파일명을 얻는다. 확장자는 제외한다.
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameOnly(String filePath) {
		String fileNameAndExt = getFileNameAndExtension(filePath);
		if (fileNameAndExt == null || fileNameAndExt.length() == 0) {
			return "";
		}

		int idxLastDot = fileNameAndExt.lastIndexOf(".");
		if (idxLastDot > -1) {
			return fileNameAndExt.substring(0, idxLastDot);
		}
		return fileNameAndExt;
	}

	/**
	 * 파일 패스를 넘기면 확장자를 얻는다. 파일명은 제외한다.
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileExtensionOnly(String filePath) {
		String fileNameAndExt = getFileNameAndExtension(filePath);
		if (fileNameAndExt == null || fileNameAndExt.length() == 0) {
			return "";
		}

		int idxLastDot = fileNameAndExt.lastIndexOf(".");
		if (idxLastDot > -1) {
			return fileNameAndExt.substring(idxLastDot + 1);
		}
		return "";
	}

	public static boolean getIsSamePath(String path1, String path2) {
		if (path1 == null || path1.length() == 0) {
			System.err.println("getIsSamePath : path1 == null || path1.length() == 0");
			return false;
		}

		if (path2 == null || path2.length() == 0) {
			System.err.println("getIsSamePath : path2 == null || path2.length() == 0");
			return false;
		}

		path1 = revisePathToValid(path1).toLowerCase();
		path2 = revisePathToValid(path2).toLowerCase();

		if (path1.equals(path2)) {
			return true;
		}

		return false;
	}

	public static String removeAfterQuestionMark(String str) {
		if (str == null) {
			return "";
		}

		int idxQuestion = str.indexOf("?");
		if (idxQuestion > -1) {
			return str.substring(0, idxQuestion);
		}

		return str;
	}
}
