/*
 * ServletRouter.java
 * May 2, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */

package plugin;

import java.util.HashMap;
import java.util.Map;

import logic.request.DeleteRequestHandler;
import logic.request.GetRequestHandler;
import logic.request.IHTTPRequest;
import logic.request.PostRequestHandler;
import logic.request.PutRequestHandler;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServletRouter {

	private Map<String, IHTTPRequest> requestMap;
	private Map<String, UriStore> rootContextMap;
	public static final String getCmd = "get";
	public static final String postCmd = "post";
	public static final String putCmd = "put";
	public static final String deleteCmd = "delete";

	public ServletRouter() {
		this.requestMap = new HashMap<>();
		this.rootContextMap = new HashMap<>();
		initializeHTTPRequestMap();
	}

	private void initializeHTTPRequestMap() {
		IHTTPRequest getRequest = new GetRequestHandler();
		IHTTPRequest postRequest = new PostRequestHandler();
		IHTTPRequest putRequest = new PutRequestHandler();
		IHTTPRequest deleteRequest = new DeleteRequestHandler();

		this.requestMap.put(getCmd, getRequest);
		this.requestMap.put(postCmd, postRequest);
		this.requestMap.put(putCmd, putRequest);
		this.requestMap.put(deleteCmd, deleteRequest);
	}

	public IHTTPRequest getRequest(String method, String uri) {
		String[] uriParse = uri.split("/");
		if (uriParse.length > 1) {
			String rootContext = uriParse[1].trim().toLowerCase();
			UriStore uriStore = this.rootContextMap.get(rootContext);
			String relativeUri = "";
			for (int i = 2; i<uriParse.length-1; i++){
				if (uriParse[i].length() > 0){
					relativeUri += ("/" + uriParse[i]);
				}
			}
			
			if (relativeUri.length() == 0 && uriParse.length==3){
				relativeUri = "/" + uriParse[2];
			}
			
			if (uriStore != null) {
				if (uriStore.getPermission(method)) {
					return uriStore.getServlet(method, relativeUri);
				} else {
					return null;
				}
			}
		}
		return this.requestMap.get(method);
	}

	public void addRootContextForServlet(String rootContext, UriStore uriStore) {
		this.rootContextMap.put(rootContext.trim().toLowerCase(), uriStore);
	}

	public void deleteServlet(String rootContext) {
		this.rootContextMap.remove(rootContext.trim().toLowerCase());
	}

}
