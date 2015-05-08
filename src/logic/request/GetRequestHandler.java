/*
 * GetRequestHandler.java
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

package logic.request;

import java.io.File;

import logic.action.ReadActionHandler;
import logic.action.RequestActionProcessor;
import logic.response.HttpResponse;
import logic.response.IHTTPResponse;
import logic.response.NotFound404ResponseHandler;
import protocol.Cache;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class GetRequestHandler extends AbstractHTTPRequest {

	public GetRequestHandler() {
		super();
	}

	@Override
	public HttpResponse handleRequest(String rootDirectory, String uri, String content, IHTTPResponse response) {
		File file = super.getFile(rootDirectory+uri);
		if (file.exists()) {
			RequestActionProcessor requestProcessor = new RequestActionProcessor();
			requestProcessor.addHandler(new ReadActionHandler());
			HttpResponse httpResponse = requestProcessor.getResponse(file, content); 
			Protocol.cacheMap.put(rootDirectory+uri, new Cache(httpResponse));
			return httpResponse;
		} else {
			return new NotFound404ResponseHandler()
					.handleResponse(Protocol.CLOSE);
		}
	}

}
