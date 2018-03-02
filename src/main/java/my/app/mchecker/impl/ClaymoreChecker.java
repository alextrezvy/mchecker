package my.app.mchecker.impl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.mchecker.Checker;

/**
 * Class for checking of Claymore's AMD GPU miner.  
 */
public class ClaymoreChecker extends TcpRequestChecker implements Checker {
	
	public static final String REQUEST = "{\"id\":0,\"jsonrpc\":\"2.0\",\"method\":\"miner_getstat1\"}";
	
	private static final Logger log = Logger.getLogger(ClaymoreChecker.class.getName());

	private String host;
	private int port;
	private String request;
	
	public ClaymoreChecker(String host, int port, String request) {
		this.host = host;
		this.port = port;
		this.request = request;
	}
	
	public boolean check() {
		boolean result = true;
		try {
			String response = sendRequest(host, port, request);
			ObjectMapper om = new ObjectMapper();
			Response respObj = om.readValue(response, Response.class);
			if ( !((respObj.error == null) || "null".equals(respObj.error)) ) {
				result = false;
			} else {
				log.log(Level.INFO, "Hashrate: {0}", respObj.result.get(3));
				log.log(Level.INFO, "Temperature|Fan: {0}", respObj.result.get(6));
			}
		} catch (IOException e) {
			log.log(Level.INFO, e.getMessage());
			result = false;
		}
		return result;
	}

	static class Response {
		int id;
		List<String> result;
		String error;
		
		public void setId(int value) {
			this.id = value;
		}
		
		public void setResult(List<String> value) {
			this.result = value;
		}
		
		public void setError(String value) {
			this.error = value;
		}
	}

}
