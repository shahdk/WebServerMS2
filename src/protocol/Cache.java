package protocol;

import logic.response.HttpResponse;

public class Cache {

	private HttpResponse response;
	private long aliveDate;
	
	public Cache(HttpResponse response){
		this.response = response;
		this.aliveDate = (System.currentTimeMillis() + 600000);
	}
	
	public HttpResponse getResponse(){
		return this.response;
	}
	
	public long getAliveDate(){
		return this.aliveDate;
	}
}
