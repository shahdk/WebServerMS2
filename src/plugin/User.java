package plugin;

public class User {

	private String username;
	private String name;
	private String hobbies;
	
	public User(){
		this.username = "";
		this.name = "";
		this.hobbies = "";
	}
	
	public User (String username, String hobbies){
		this.username = username;
		this.name = "";
		this.hobbies = hobbies;
	}
	
	public User (String username, String name, String hobbies){
		this.username = username;
		this.name = name;
		this.hobbies = hobbies;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}
}
