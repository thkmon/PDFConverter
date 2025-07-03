package com.bb;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thkmon.pdfconv.prototype.FilePathList;
import com.thkmon.pdfconv.util.FilePathUtil;

public class BBThumbnailMaker {

	public static void makeThumbnail() {
		
		try {
			String includePattern = "*";
			String exceptPattern = "";

			FilePathList pathList = FilePathUtil.makePathList("output", includePattern, exceptPattern);

			if (pathList == null || pathList.size() == 0) {
				return;
			}
			
			int maxWidth = 4096; // 원하는 썸네일 너비
			int maxHeight = 4096; // 원하는 썸네일 높이
			
			String path = null;
			int pathCount = pathList.size();
			for (int i = 0; i < pathCount; i++) {
				path = pathList.get(i);
				System.out.println((i+1) + "/" + pathCount + " : " + path);

				String extOnly = FilePathUtil.getFileExtensionOnly(path);
				String nameAndExt = FilePathUtil.getFileNameAndExtension(path);

				String outputImagePath = null;
				if (maxWidth == 4096) {
					outputImagePath = "output2/" + nameAndExt;
				}
				
				File file = new File(outputImagePath);
				if (!file.exists()) {
					makeThumbnail(path, outputImagePath, maxWidth, maxHeight);	
				}
			}

			// FilePathUtil.getInstance().get
			//
			// String outputImagePath = "thumbnail." + extOnly; // 생성될 썸네일 이미지 경로
			// int maxWidth = 150; // 원하는 썸네일 너비
			// int maxHeight = 150; // 원하는 썸네일 높이
			//
			// makeThumbnail(inputImagePath, outputImagePath, maxWidth, maxHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void makeThumbnail(String inputImagePath, String outputImagePath, int maxWidth, int maxHeight) {
		BufferedImage originalImage = null;
		BufferedImage thumbnail = null;
		Graphics2D g2d = null;

		try {
			// 원본 이미지 읽기
			originalImage = ImageIO.read(new File(inputImagePath));

			// 원본 이미지 크기
			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();

			// 비율 유지하면서 크기 조정
			double scale = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
			int newWidth = (int) (originalWidth * scale);
			int newHeight = (int) (originalHeight * scale);

			// 썸네일 이미지 생성
			Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			g2d = thumbnail.createGraphics();
			g2d.drawImage(resizedImage, 0, 0, null);
			g2d.dispose();

			// 파일 확장자 확인
			String formatName = "";
			if (outputImagePath.lastIndexOf(".") > -1) {
				formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
			} else {
				formatName = "png";
			}

			// 이미지 저장
			ImageIO.write(thumbnail, formatName, new File(outputImagePath));

//			System.out.println("썸네일 생성 완료: " + outputImagePath);

		} catch (IOException e) {
			System.err.println("이미지 처리 중 오류 발생: " + e.getMessage());

		} finally {
			if (g2d != null) {
				g2d.dispose();
			}

			originalImage = null;
			thumbnail = null;
		}
	}
}