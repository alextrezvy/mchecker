package my.app.mchecker.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import my.app.mchecker.Checker;

/**
 * Class for checking of SGMiner-GM miner. 
 */
public class SGMinerChecker extends TcpRequestChecker implements Checker {

	private static final Logger log = Logger.getLogger(SGMinerChecker.class.getName());

	private String host;
	private int port;
	private String request;
	
	public SGMinerChecker(String host, int port, String request) {
		this.host = host;
		this.port = port;
		this.request = request;
	}	
	
	@Override
	public boolean check() {
		boolean result = true;
		try {
			String response = sendRequest(host, port, request);
			if (response.isEmpty()) {
				result = false;
			} else {
				log.log(Level.INFO, response);
			}	
		} catch (IOException e) {
			log.log(Level.INFO, e.getMessage());
			result = false;
		}
		
		return result;
	}

}
