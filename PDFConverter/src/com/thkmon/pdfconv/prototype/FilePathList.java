package com.thkmon.pdfconv.prototype;

import com.thkmon.pdfconv.util.FilePathUtil;

public class FilePathList extends StringList {

	@Override
	public boolean add(String e) {
		if (e == null || e.length() == 0) {
			return false;
		}

		e = FilePathUtil.revisePathToValid(e);
		return super.add(e);
	}

	@Override
	public void add(int index, String e) {
		if (e == null || e.length() == 0) {
			return;
		}

		e = FilePathUtil.revisePathToValid(e);
		super.add(index, e);
	}

	// 널이 반환되지 않도록 조치
	@Override
	public String get(int arg0) {
		if (super.get(arg0) == null) {
			return "";
		}
		return super.get(arg0);
	}

	public boolean hasString(String str, boolean inWebappDir) {
		if (str == null || str.length() == 0) {
			System.err.println("str == null || str.length() == 0");
			return false;
		}

		str = revisePath(str);

		{
			if (inWebappDir) {
				int webappIndex = str.indexOf("webapp");
				int slashNextWebappIndex = -1;
				if (webappIndex > -1) {
					slashNextWebappIndex = str.indexOf("/", webappIndex);

					if (slashNextWebappIndex > -1) {
						str = str.substring(slashNextWebappIndex);
					}
				}
			}
		}

		// System.out.println("str : " + str);

		int count = this.size();

		String onePath = null;
		for (int i = 0; i < count; i++) {
			onePath = this.get(i);
			if (onePath == null) {
				continue;
			}

			onePath = revisePath(onePath);

			{
				if (inWebappDir) {
					int webappIndex = onePath.indexOf("webapp");
					int slashNextWebappIndex = -1;
					if (webappIndex > -1) {
						slashNextWebappIndex = onePath.indexOf("/", webappIndex);

						if (slashNextWebappIndex > -1) {
							onePath = onePath.substring(slashNextWebappIndex);
						}
					}
				}
			}

			if (str.equals(onePath)) {
				return true;
			}
			// else {
			// System.out.println("onePath : " + onePath);
			// }
		}
		return false;
	}

	private String revisePath(String onePath) {
		if (onePath == null || onePath.length() == 0) {
			return "";
		}

		onePath = onePath.replace("\\", "/");
		while (onePath.indexOf("//") > -1) {
			onePath = onePath.replace("//", "/");
		}

		onePath = onePath.trim();

		if (!onePath.matches("[a-zA-Z]:.*")) {
			if (!onePath.startsWith("/")) {
				onePath = "/" + onePath;
			}
		}

		return onePath;
	}
}
