package cn.guyu.entity;

public class Haha {

	public Haha() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Haha(String ct, String title, String img) {
		super();
		this.ct = ct;
		this.title = title;
		this.img = img;
	}
	private String  ct;
	private String title;
	private String  img;
	
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	@Override
	public String toString() {
		return "Haha [ct=" + ct + ", title=" + title + ", img=" + img + "]";
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	
}
