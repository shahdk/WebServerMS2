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

import java.io.File;
import java.io.InputStream;
import logic.request.HttpRequest;
import logic.request.IHTTPRequest;
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
	private ServletRouter servletRouter;
	
	
	public URLParser(InputStream inStream, ConnectionHandler connHandler, ServletRouter servletRouter) {
		this.inStream = inStream;
		this.connHandler = connHandler;
		this.response = null;
		this.request = null;
		this.servletRouter = servletRouter;
	}

	public HttpResponse parseURL() {

		try {
			request = HttpRequest.read(inStream);
			System.out.println(request);
		} catch (ProtocolException pe) {
			int status = pe.getStatus();
			if (status == Protocol.BAD_REQUEST_CODE) {
				response = create400BadRequest();
			} else if(status == Protocol.NOT_SUPPORTED_CODE){
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
	
	private HttpResponse handleRequest() {

		try {
			if (!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
				response = create505NotSupported();
			} else {
				response = getResponseForRequest(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response; 
	}
	
	private HttpResponse getResponseForRequest(HttpRequest request){
		
		// Map<String, String> header = request.getHeader();
		// String date = header.get("if-modified-since");
		// String hostName = header.get("host");
		
		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();
		String method = request.getMethod().trim().toLowerCase();
		IHTTPRequest httpRequest = this.servletRouter.getRequest(method, uri);
		File file = httpRequest.getFile(rootDirectory, uri);
		return httpRequest.handleRequest(file, new String(request.getBody()));
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
