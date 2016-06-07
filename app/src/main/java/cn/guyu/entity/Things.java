package cn.guyu.entity;

import java.util.List;

public class Things {
	private int thingid;
	private String thingtext;
	private String thingdata;
	
	private List<String> thingimgs;//图片集合
	
	private String thinglng;
	private String thinglat;
	
	private String thingmapimg;
	private String thingmaptext;
	private String thingzan;
	private int label;
	
	private String userid;
	private String username;
	private String userimg;

	

	
	@Override
	public String toString() {
		return "Things [thingid=" + thingid + ", thingtext=" + thingtext
				+ ", thingdata=" + thingdata + ", thingimgs=" + thingimgs
				+ ", thinglng=" + thinglng + ", thinglat=" + thinglat
				+ ", thingmapimg=" + thingmapimg + ", thingmaptext="
				+ thingmaptext + ", thingzan=" + thingzan + ", label=" + label
				+ ", userid=" + userid + ", username=" + username
				+ ", userimg=" + userimg + "]";
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public int getThingid() {
		return thingid;
	}

	public void setThingid(int thingid) {
		this.thingid = thingid;
	}
	
	
	
	
	
	public String getThingtext() {
		return thingtext;
	}

	public void setThingtext(String thingtext) {
		this.thingtext = thingtext;
	}
	
	
  
	public String getThingdata() {
		return thingdata;
	}

	public void setThingdata(String thingdata) {
		this.thingdata = thingdata;
	}
	
	
	
	public List<String> getThingimgs() {
		return thingimgs;
	}
	public void setThingimgs(List<String> thingimgs) {
		this.thingimgs = thingimgs;
	}

	public String getThingmaptext() {
		return thingmaptext;
	}

	public void setThingmaptext(String thingmaptext) {
		this.thingmaptext = thingmaptext;
	}




	public String getThinglng() {
		return thinglng;
	}

	public void setThinglng(String thinglng) {
		this.thinglng = thinglng;
	}





	public String getThinglat() {
		return thinglat;
	}

	public void setThinglat(String thinglat) {
		this.thinglat = thinglat;
	}





	public String getThingmapimg() {
		return thingmapimg;
	}

	public void setThingmapimg(String thingmapimg) {
		this.thingmapimg = thingmapimg;
	}





	public String getThingzan() {
		return thingzan;
	}

	public void setThingzan(String thingzan) {
		this.thingzan = thingzan;
	}





	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}





	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	

	public int getLabel() {
		return this.label;
	}

	public void setLabel(int label) {
		this.label = label;
	}



}