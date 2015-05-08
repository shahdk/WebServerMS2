/*
 * URLParser.java
 * Apr 25, 2015
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

package server;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import logic.request.DeleteRequestHandler;
import logic.request.GetRequestHandler;
import logic.request.HttpRequest;
import logic.request.IHTTPRequest;
import logic.request.PostRequestHandler;
import logic.request.PutRequestHandler;
import logic.response.BadRequest400ResponseHandler;
import logic.response.HttpResponse;
import logic.response.NotSupported505ResponseHandler;
import plugin.ServletRouter;
import protocol.Protocol;
import protocol.ProtocolException;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class URLParser {

	private InputStream inStream;
	private ConnectionHandler connHandler;
	private HttpResponse response;
	private HttpRequest request;
	private Map<String, IHTTPRequest> requestMap;
	private final String getCmd = "get";
	private final String postCmd = "post";
	private final String putCmd = "put";
	private final String deleteCmd = "delete";
	private ServletRouter servletRouter;

	public URLParser(InputStream inStream, ConnectionHandler connHandler,
			ServletRouter servletRouter) {
		this.inStream = inStream;
		this.connHandler = connHandler;
		this.response = null;
		this.request = null;
		this.requestMap = new HashMap<>();
		this.servletRouter = servletRouter;
		initializeHTTPRequestMap();
	}

	public HttpResponse parseURL() {

		try {
			request = HttpRequest.read(inStream);
			System.out.println(request);
		} catch (ProtocolException pe) {
			int status = pe.getStatus();
			if (status == Protocol.BAD_REQUEST_CODE) {
				response = create400BadRequest();
			} else if (status == Protocol.NOT_SUPPORTED_CODE) {
				response = create505NotSupported();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = create400BadRequest();
		}

		if (response != null) {
			return response;
		}

		return handleRequest();
	}

	private void initializeHTTPRequestMap() {
		IHTTPRequest getRequest = new GetRequestHandler();
		IHTTPRequest postRequest = new PostRequestHandler();
		IHTTPRequest putRequest = new PutRequestHandler();
		IHTTPRequest deleteRequest = new DeleteRequestHandler();

		this.requestMap.put(this.getCmd, getRequest);
		this.requestMap.put(this.postCmd, postRequest);
		this.requestMap.put(this.putCmd, putRequest);
		this.requestMap.put(this.deleteCmd, deleteRequest);
	}

	private HttpResponse handleRequest() {
		response = create400BadRequest();
		try {
			if (!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
				response = create505NotSupported();
			} else if (request.getMethod().equalsIgnoreCase(Protocol.GET)) {
				response = createGetRequest(request);
			} else if (request.getMethod().equalsIgnoreCase(Protocol.POST)) {
				response = createPostRequest(request);
			} else if (request.getMethod().equalsIgnoreCase(Protocol.PUT)) {
				response = createPutRequest(request);
			} else if (request.getMethod().equalsIgnoreCase(Protocol.DELETE)) {
				response = createDeleteRequest(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @param request
	 */
	private HttpResponse createDeleteRequest(HttpRequest request) {

		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();

		IHTTPRequest deleteRequest = this.servletRouter.getRequest(
				this.deleteCmd, uri);
		if(deleteRequest == null){
			return create400BadRequest();
		}
		String[] uriParse = uri.split("/");
		if (uriParse.length > 1) {
			uri = "/" + uriParse[uriParse.length - 1];
		}
		return deleteRequest.handleRequest(rootDirectory, uri, "",
				new BadRequest400ResponseHandler());
	}

	/**
	 * @param request
	 */
	private HttpResponse createPutRequest(HttpRequest request) {
		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();

		IHTTPRequest putRequest = this.servletRouter.getRequest(this.putCmd,
				uri);
		if(putRequest == null){
			return create400BadRequest();
		}
		String[] uriParse = uri.split("/");
		if (uriParse.length > 1) {
			uri = "/" + uriParse[uriParse.length - 1];
		}
		return putRequest.handleRequest(rootDirectory, uri,
				new String(request.getBody()),
				new BadRequest400ResponseHandler());
	}

	/**
	 * @param request
	 */
	private HttpResponse createPostRequest(HttpRequest request) {
		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();

		IHTTPRequest postRequest = this.servletRouter.getRequest(this.postCmd,
				uri);
		if(postRequest == null){
			return create400BadRequest();
		}
		String[] uriParse = uri.split("/");
		if (uriParse.length > 1) {
			uri = "/" + uriParse[uriParse.length - 1];
		}
		String content = new String(request.getBody());
		String filename = this.getFileName(content);
		if (filename.length() > 0) {
			uri = "/" + filename;
			content = getContent(content);
		}

		return postRequest.handleRequest(rootDirectory, uri, content,
				new BadRequest400ResponseHandler());
	}

	private String getFileName(String content) {
		String fileName = "";
		if (content.contains("filename=\"")) {
			String[] contentParse = content.split("\n");
			String[] headers = contentParse[1].split(";");
			for (int i = 0; i < headers.length; i++) {
				if (headers[i].contains("filename")) {
					String[] values = headers[i].split("=");
					fileName = values[1].replaceAll("\"", "");
					fileName = fileName.replaceAll("\r", "");
				}
			}
		}
		return fileName;
	}
	
	private String getContent(String content){
		String actualContent = "";
		boolean isContent = false;
		String[] contentParse = content.split("\n");
		for (int i = 0; i < contentParse.length; i++) {
			if (isContent){
				actualContent += contentParse[i];
			}
			if (contentParse[i].trim().length() == 0) {
				isContent = true;
			}
			if (i == contentParse.length-2){
				isContent = false;
			}
		}
		return actualContent;
	}

	private HttpResponse createGetRequest(HttpRequest request) {
		// Map<String, String> header = request.getHeader();
		// String date = header.get("if-modified-since");
		// String hostName = header.get("host");
		//

		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();

		IHTTPRequest getRequest = this.servletRouter.getRequest(this.getCmd,
				uri);

		if(getRequest == null){
			return create400BadRequest();
		}
		
		String[] uriParse = uri.split("/");
		if (uriParse.length > 1) {
			uri = "/" + uriParse[uriParse.length - 1];
		}
		return getRequest.handleRequest(rootDirectory, uri, "",
				new BadRequest400ResponseHandler());
	}

	private HttpResponse create505NotSupported() {
		HttpResponse response;
		response = new NotSupported505ResponseHandler()
				.handleResponse(Protocol.CLOSE);
		return response;
	}

	private HttpResponse create400BadRequest() {
		HttpResponse response;
		response = new BadRequest400ResponseHandler()
				.handleResponse(Protocol.CLOSE);
		return response;
	}

	/**
	 * @return
	 */
	public HttpResponse getResponse() {
		return this.parseURL();
	}

}
