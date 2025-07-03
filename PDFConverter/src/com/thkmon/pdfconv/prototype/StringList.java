package com.thkmon.pdfconv.prototype;

import java.util.ArrayList;

public class StringList extends ArrayList<String> {

	/**
	 * indexOfRow 를 대신 사용할 것을 권장한다.
	 */
	@Deprecated
	public int indexOf(Object str) {
		if (str == null) {
			return -1;
		}
		return indexOfRow(String.valueOf(str), 0, false);
	}

	/**
	 * 특정 스트링이 포함된 row 번호를 리턴한다.
	 */
	public int indexOfRow(String str) {
		return indexOfRow(str, 0, false);
	}

	/**
	 * 특정 스트링이 포함된 row 번호를 리턴한다.
	 */
	public int indexOfRow(String str, int axisRow) {
		return indexOfRow(str, axisRow, false);
	}

	/**
	 * 특정 스트링이 포함된 row 번호를 리턴한다.
	 */
	public int indexOfRowIgnoreCase(String str) {
		return indexOfRow(str, 0, true);
	}

	/**
	 * 특정 스트링이 포함된 row 번호를 리턴한다.
	 */
	public int indexOfRowIgnoreCase(String str, int axisRow) {
		return indexOfRow(str, axisRow, true);
	}

	/**
	 * 특정 스트링이 포함된 row 번호를 리턴한다.
	 */
	private int indexOfRow(String str, int axisRow, boolean ignoreCase) {
		if (str == null || str.length() == 0) {
			return -1;
		}

		if (axisRow < 0) {
			axisRow = 0;
		}

		if (ignoreCase) {
			str.toLowerCase();
		}

		int count = this.size();
		if (count < 1) {
			return -1;
		}

		int idx = -1;

		String oneLine = null;
		for (int row = axisRow; row < count; row++) {
			oneLine = this.get(row);
			if (oneLine == null || oneLine.length() == 0) {
				continue;
			}

			if (ignoreCase) {
				idx = oneLine.toLowerCase().indexOf(str);

			} else {
				idx = oneLine.indexOf(str);
			}

			if (idx > -1) {
				return row;
			}
		}

		return -1;
	}

	/**
	 * 특정 스트링과 정규식으로 매치되는 row 번호 목록을 리턴한다.
	 * 
	 * @param str
	 * @param axisRow
	 * @param ignoreCase
	 * @return
	 */
	public IntegerList indexOfRowList(String str, int axisRow, boolean ignoreCase) {
		if (str == null || str.length() == 0) {
			return null;
		}

		if (axisRow < 0) {
			axisRow = 0;
		}

		if (ignoreCase) {
			str.toLowerCase();
		}

		int count = this.size();
		if (count < 1) {
			return null;
		}

		IntegerList resultList = new IntegerList();
		int idx = -1;

		String oneLine = null;
		for (int row = axisRow; row < count; row++) {
			oneLine = this.get(row);
			if (oneLine == null || oneLine.length() == 0) {
				continue;
			}

			if (ignoreCase) {
				idx = oneLine.toLowerCase().indexOf(str);

			} else {
				idx = oneLine.indexOf(str);
			}

			if (idx > -1) {
				resultList.add(row);
			}
		}

		return resultList;
	}

	/**
	 * 특정 스트링과 정규식으로 매치되는 row 번호를 리턴한다.
	 */
	public int indexOfRowMatchingRegex(String regex) {
		if (regex == null || regex.length() == 0) {
			return -1;
		}

		int axisRow = 0;

		int count = this.size();
		if (count < 1) {
			return -1;
		}

		String oneLine = null;
		for (int row = axisRow; row < count; row++) {
			oneLine = this.get(row);
			if (oneLine == null || oneLine.length() == 0) {
				continue;
			}

			if (oneLine.matches(regex)) {
				return row;
			}
		}

		return -1;
	}

	/**
	 * 특정 스트링과 정규식으로 매치되는 스트링 목록을 리턴한다.
	 */
	public StringList getRowListMatchingRegex(String regex) {
		if (regex == null || regex.length() == 0) {
			return null;
		}

		int axisRow = 0;

		int count = this.size();
		if (count < 1) {
			return null;
		}

		StringList resultList = new StringList();

		String oneLine = null;
		for (int row = axisRow; row < count; row++) {
			oneLine = this.get(row);
			if (oneLine == null || oneLine.length() == 0) {
				continue;
			}

			if (oneLine.matches(regex)) {
				resultList.add(oneLine);
			}
		}

		return resultList;
	}

	public StringList() {
		super();
	}

	/**
	 * 중복 허용하지 않고 엘리먼트 추가한다.
	 * 
	 * @param e
	 * @return
	 */
	public boolean addNotAllowDuplicate(String e) {
		if (e != null) {
			String ee = null;
			int count = this.size();
			for (int i = 0; i < count; i++) {
				ee = get(i);
				if (ee == null) {
					continue;
				}

				if (ee.equals(e)) {
					// 중복
					return false;
				}
			}
		}

		return super.add(e);
	}

	public StringList(String... strings) {
		super();
		int count = strings.length;

		String oneStr = null;
		for (int i = 0; i < count; i++) {
			oneStr = strings[i];
			if (oneStr == null || oneStr.length() == 0) {
				continue;
			}

			this.add(oneStr);
		}
	}

	/**
	 * 어레이리스트 내용을 문자열로 변환한다.
	 */
	public String toString() {
		int lineCount = size();

		if (lineCount == 0) {
			System.err.println("StringList : lineCount == 0");
			return "";
		}

		String text = "";
		StringBuffer buff = new StringBuffer();

		for (int i = 0; i < lineCount; i++) {
			text = get(i);
			buff.append(i + " : " + (text == null ? "{null}" : text));
			buff.append("\n");
		}

		return buff.toString();
	}

	/**
	 * 전체 스트링을 이어붙인다. (delimiter 가 스트링 사이사이에 들어간다.)
	 * 
	 * @param delimiter
	 * @return
	 */
	public String toSerialText(String delimiter) {
		if (delimiter == null) {
			delimiter = "";
		}

		StringBuffer resultStr = new StringBuffer();

		int count = this.size();
		String oneStr = null;
		for (int i = 0; i < count; i++) {
			oneStr = this.get(i);

			if (oneStr == null || oneStr.length() == 0) {
				continue;
			}

			resultStr.append(oneStr);

			if (delimiter != null && delimiter.length() > 0) {
				resultStr.append(delimiter);
			}
		}

		if (delimiter != null && delimiter.length() > 0) {
			if (resultStr.toString().endsWith(delimiter)) {
				resultStr.delete(resultStr.length() - delimiter.length(), resultStr.length());
			}
		}

		System.out.println(resultStr.toString());
		return resultStr.toString();
	}

	public StringList getClone() {
		StringList resultList = new StringList();

		int strCount = this.size();
		for (int i = 0; i < strCount; i++) {
			resultList.add(this.get(i));
		}

		return resultList;
	}

	public void appendList(StringList stringList) {
		if (stringList == null || stringList.size() == 0) {
			return;
		}

		String onePath = null;
		int count = stringList.size();

		for (int i = 0; i < count; i++) {
			onePath = stringList.get(i);
			if (onePath == null || onePath.length() == 0) {
				continue;
			}
			this.add(onePath);
		}
	}

	public void appendListNotAllowDuplicate(StringList stringList) {
		if (stringList == null || stringList.size() == 0) {
			return;
		}

		String onePath = null;
		int count = stringList.size();

		for (int i = 0; i < count; i++) {
			onePath = stringList.get(i);
			if (onePath == null || onePath.length() == 0) {
				continue;
			}
			this.addNotAllowDuplicate(onePath);
		}
	}
}
