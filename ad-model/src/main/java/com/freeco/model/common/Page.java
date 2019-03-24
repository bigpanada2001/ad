package com.freeco.model.common;

import java.util.List;

public class Page<BEAN> {

	private int count;

	private List<BEAN> data;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<BEAN> getData() {
		return data;
	}

	public void setData(List<BEAN> data) {
		this.data = data;
	}

	public static int getCount(Page<?> page) {
		if (page == null) {
			return 0;
		}
		else {
			return page.getCount();
		}
	}

	public static <BEAN> List<BEAN> getData(Page<BEAN> page) {
		if (page == null) {
			return null;
		}
		else {
			return page.getData();
		}
	}
}