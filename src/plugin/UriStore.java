package plugin;

import java.util.HashMap;
import java.util.Map;

import logic.request.IHTTPRequest;

public class UriStore {

	private Map<String, String> methodUriMap;
	private Map<String, IHTTPRequest> uriServletMap;
	private Map<String, Boolean> permissionsUriMap;
	
	public UriStore(){
		this.methodUriMap = new HashMap<>();
		this.uriServletMap = new HashMap<>();
		this.permissionsUriMap = new HashMap<>();
		this.permissionsUriMap.put("get", false);
		this.permissionsUriMap.put("post", false);
		this.permissionsUriMap.put("put", false);
		this.permissionsUriMap.put("delete", false);
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
		if (uri == null){
			return null;
		}
		return this.uriServletMap.get(uri.toLowerCase());
	}
	
	public void setPermission(String method, boolean value){
		this.permissionsUriMap.put(method.trim().toLowerCase(), value);
	}
	
	public boolean getPermission(String method){
		return this.permissionsUriMap.get(method.trim().toLowerCase());
	}
	
}
