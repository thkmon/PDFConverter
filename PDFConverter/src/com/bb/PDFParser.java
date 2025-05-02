package com.bb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFParser {

	
	public static void main(String[] args) {
		
		System.out.println("시작");
		
		try {
			ArrayList<String> stringList = new ArrayList<String>();
			
			ArrayList<String> fileList = FileReadUtil.readFile(new File("input/fms_input.txt"));
			// ArrayList<String> fileList = FileReadUtil.readFile(new File("input/fms_input2.txt"));
			for (int i=0; i<fileList.size(); i++) {
				System.out.println(fileList.get(i));
				
				String dd = fileList.get(i);
				
				if (dd.startsWith("--")) {
					continue;
				}
				
				String[] ddArr = dd.split("\t");
				
				// SELECT ERROR_CK_NO, BUSI_CENTER, BUSI_CEN_CD, BUILD_NM, PDF_FILE_PATH, PDF_FILE_NAME FROM EAM_ERROR_CHECK WHERE PDF_FILE_NAME IS NOT NULL ORDER BY ERROR_CK_NO DESC;
				String ERROR_CK_NO = ddArr[0];
				String BUSI_CENTER = ddArr[1];
				String BUSI_CEN_CD = ddArr[2];
				String BUILD_NM = ddArr[3];
				String PDF_FILE_PATH = ddArr[4];
				String PDF_FILE_NAME = ddArr[5];
				
				// File file = new File("D:\\fms\\202493_92227_C24000001722832.pdf"); // 분석할 PDF 경로
				File file = new File("D:\\fms\\" + PDF_FILE_NAME); // 분석할 PDF 경로
				
				String PDF_BUSI_CENTER = "";
				String PDF_BUILD_NM = "";
				
				PDDocument document = PDDocument.load(file);
	            if (!document.isEncrypted()) {
	                PDFTextStripper stripper = new PDFTextStripper();
	                String text = stripper.getText(document);
	                // System.out.println("PDF 내용:\n" + text);
	                
	                String[] lineArr = text.split("\n");
	                for (int k=0; k<lineArr.length; k++) {
	                	// System.out.println(lineArr[k]);
	                	
	                	if (lineArr[k].indexOf("총 괄 국") > -1 && lineArr[k].indexOf("발생장소") > -1) {
	                		lineArr[k] = lineArr[k].replace("총 괄 국", "총괄국");
	                		lineArr[k] = lineArr[k].replace("\r", "");
	                		lineArr[k] = lineArr[k].replace("\n", "");
	                		
	                		String[] aaa = lineArr[k].split(" ");
	                		String chongLabel = aaa[0];
	                		PDF_BUSI_CENTER = aaa[1];
	                		String labelPlace = aaa[2];
	                		PDF_BUILD_NM = aaa[3];
	                		if (chongLabel.equals("총괄국") && labelPlace.equals("발생장소"))  {
	                			//System.out.println("총괄국 : " + PDF_BUSI_CENTER + " / 발생장소 : " + PDF_BUILD_NM);
	                    		break;
	                		}
	                		
	                	}
	                }
	                
	                if (PDF_BUSI_CENTER.length() == 0 || PDF_BUILD_NM.length() == 0) {
	                	System.err.println("못찾았음!!!");
	                	throw new Exception("못찾았음");
	                }
	                
	                if (BUSI_CENTER.equals(PDF_BUSI_CENTER) && BUILD_NM.equals(PDF_BUILD_NM)) {
	                	System.out.println("같다");
	                	
	                } else {
	                	System.out.println("틀림");
	                	System.out.println(BUSI_CENTER.equals(PDF_BUSI_CENTER));
	                	System.out.println(BUILD_NM.equals(PDF_BUILD_NM));
	                	
	                	System.out.println("BUSI_CENTER : " + BUSI_CENTER + " / chong" + PDF_BUSI_CENTER + " / BUILD_NM : [" + BUILD_NM + "] / PDF_BUILD_NM : [" + PDF_BUILD_NM + "]");
	                	String sql = "UPDATE EAM_ERROR_CHECK SET BUSI_CENTER = '" + PDF_BUSI_CENTER + "', BUILD_NM = '" + PDF_BUILD_NM + "', BUSI_CEN_CD = (SELECT BUSI_CEN_CD FROM EAM_BUSI_MST WHERE BUILD_NM = '" + PDF_BUILD_NM + "' AND USE_YN = 'Y') WHERE ERROR_CK_NO = '" + ERROR_CK_NO + "' AND BUSI_CENTER = '" + BUSI_CENTER + "' AND BUILD_NM = '" + BUILD_NM + "';";
	                	System.out.println(sql);
	                	stringList.add(sql + "\n");
//	                	break;
	                }
	                
	            } else {
	                System.out.println("이 PDF는 암호화되어 있어 읽을 수 없습니다.");
	            }
	            
//	            if (i==0) {
//	            	break;
//	            }
			}
			
			if (stringList.size() == 0) {
				stringList.add("결과없음(0건)");
			}
			FileWriteUtil.writeFile("output/fms_output.txt", stringList, false);
			
        } catch (Exception e) {
            System.err.println("PDF 파싱 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            
        } finally {
        	System.out.println("끝");
        }
	}
}
