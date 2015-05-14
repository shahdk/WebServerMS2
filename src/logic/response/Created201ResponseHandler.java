package logic.response;

import java.util.HashMap;

import protocol.Protocol;

public class Created201ResponseHandler extends AbstractHTTPResponse {

	private String content;

	public Created201ResponseHandler(String content) {
		this.content = content;
	}

	@Override
	public HttpResponse handleResponse(String connection) {
		HttpResponse response = new HttpResponse(Protocol.VERSION,
				Protocol.CREATED_CODE, Protocol.CREATED_TEXT,
				new HashMap<String, String>(), null);

		// Lets fill up the header fields with more information
		fillGeneralHeader(response, connection);
		// Lets get content length in bytes
		long length = (long) content.length();
		response.put(Protocol.CONTENT_LENGTH, length + "");
		response.setBody(content);
		return response;
	}

}
