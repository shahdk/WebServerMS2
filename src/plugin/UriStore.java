package plugin;

import java.util.HashMap;
import java.util.Map;

import logic.request.IHTTPRequest;

public class UriStore {

	private Map<String, String> methodUriMap;
	private Map<String, IHTTPRequest> uriServletMap;
	
	public UriStore(){
		this.methodUriMap = new HashMap<>();
		this.uriServletMap = new HashMap<>();
	}
	
	public void addUriForMethod(String method, String uri){
		this.methodUriMap.put(method.trim().toLowerCase(), uri.trim().toLowerCase());
	}
	
	public String getUriForMethod(String method){
		return this.methodUriMap.get(method.trim().toLowerCase());
	}
	
	public void addServletForUri(String uri, IHTTPRequest servlet){
		this.uriServletMap.put(uri.trim().toLowerCase(), servlet);
	}
	
	public IHTTPRequest getServletForUri(String uri){
		return this.uriServletMap.get(uri.trim().toLowerCase());
	}
	
	public IHTTPRequest getServlet(String method){
		String uri = this.methodUriMap.get(method.toLowerCase());
		return this.uriServletMap.get(uri.toLowerCase());
	}
	
}
