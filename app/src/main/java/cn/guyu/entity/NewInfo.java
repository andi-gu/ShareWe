package cn.guyu.entity;
//新闻信息实体类
public class NewInfo {

	private String title; //新闻标题
	private String detail;// 详细信息（内容）
	private Integer comment;//跟帖数量（是一个数字）
	private String imageUrl; //图片地址
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	private String contentUrl; //图片地址
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Integer getComment() {
		return comment;
	}
	@Override
	public String toString() {
		return "NewInfo [title=" + title + ", detail=" + detail + ", comment="
				+ comment + ", imageUrl=" + imageUrl + ", contentUrl="
				+ contentUrl + "]";
	}
	public void setComment(Integer comment) {
		this.comment = comment;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public NewInfo(String title, String detail, Integer comment, String imageUrl,String contentUrl) {
		super();
		this.title = title;
		this.detail = detail;
		this.comment = comment;
		this.imageUrl = imageUrl;
		this.contentUrl =contentUrl;
	}
	public NewInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
