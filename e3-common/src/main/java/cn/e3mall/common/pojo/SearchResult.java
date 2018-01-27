package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{

	private Integer page;
	private Long recourdCount;
	private Integer totalPages;
	private List<SearchItem> itemList;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Long getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(Long recourdCount) {
		this.recourdCount = recourdCount;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
	
	
}
