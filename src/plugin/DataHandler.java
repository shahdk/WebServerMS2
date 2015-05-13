package plugin;

import java.util.Map;

public interface DataHandler {

	public String readData();
	public boolean writeData();
	public Map<String, User> getData();
	public void setData(Map<String, User> usersMap);
	
}
