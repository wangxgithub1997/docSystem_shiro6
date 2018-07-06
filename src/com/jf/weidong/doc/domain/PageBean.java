package com.jf.weidong.doc.domain;

import java.util.List;

public class PageBean<T> {
	/**
	 * 当前页
	 */
	private int currentPage;
	
	//总页数
	private int totalPage;
	
	//页面的数据
	List<T> list;
	
	//每页条数
	private int pageSize = 5;
	
	//总条数
	private int totalCount;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		//总条数/每页条数
		int i = totalCount/pageSize;
		//Math.ceil((totalCount * 1.0/pageSize));
		return totalCount % pageSize == 0 ? i : i + 1;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "PageBean [currentPage=" + currentPage + ", totalPage=" + totalPage + ", pageSize="
				+ pageSize + ", totalCount=" + totalCount + "]";
	}
	
}
