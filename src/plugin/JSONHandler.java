package plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plugin.DataHandler;
import plugin.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONHandler implements DataHandler {

	private Map<String, User> usersMap;
	private Gson gson;

	public JSONHandler() {
		this.usersMap = new HashMap<>();
		this.gson = new Gson();
	}

	public String readData() {

		try {
			BufferedReader br = new BufferedReader(new FileReader("web"
					+ File.separator + "users.json"));

			Type userListType = new TypeToken<List<User>>() {}.getType();

			List<User> users = gson.fromJson(br, userListType);
			for (User user : users) {
				this.usersMap.put(user.getUsername(), user);
			}
			String json = gson.toJson(users);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean writeData() {
		
		ArrayList<User> users = new ArrayList<>();
		for (String username: this.usersMap.keySet()){
			users.add(this.usersMap.get(username));
		}
		String json = getJsonString(users);

		try {
			FileWriter writer = new FileWriter("web"
					+ File.separator + "users.json", false);
			writer.write(json);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getJsonString(ArrayList<User> users) {
		String json = gson.toJson(users);
		return json;
	}
	
	public Map<String, User> getData(){
		return this.usersMap;
	}
	
	public void setData(Map<String, User> usersMap){
		this.usersMap = new HashMap<>(usersMap);
	}
	
	public User getJSONFromString(String jsonString){
		return gson.fromJson(jsonString, User.class);
	}
}
