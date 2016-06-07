package cn.guyu.entity;

public class Users {

	private String username; // 昵称

	private String userid; // 账号
	
	private String password; // 密码

	private String usersex; //性别
	
	private String userimge; //服务器里头像的地址链接
	
	public String getUserimge() {
		return userimge;
	}

	public void setUserimge(String userimge) {
		this.userimge = userimge;
	}

	public String getUsersex() {
		return usersex;
	}

	public void setUsersex(String usersex) {
		this.usersex = usersex;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}



	

}
