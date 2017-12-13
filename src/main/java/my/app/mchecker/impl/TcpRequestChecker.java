package my.app.mchecker.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.mchecker.Checker;
import my.app.mchecker.impl.ClaymoreChecker.Response;

public class TcpRequestChecker {

	public static final int TIMEOUT = 10000;	
	
	private static final Logger log = Logger.getLogger(TcpRequestChecker.class.getName());
	
	/**
	 * @param host
	 * @param port
	 * @param request
	 * @return response 
	 */
	protected String sendRequest(String host, int port, String request) throws IOException {
		String result = "";
		Socket s = new Socket(host, port);
		s.setSoTimeout(TIMEOUT);
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();
		os.write(request.getBytes());
		os.flush();
		int read = 0;
		int cnt = 0;
		byte[] b = new byte[1024];
		while ((read = is.read()) >= 0) {
			if ((cnt+1) > b.length) {
				b = Arrays.copyOf(b, b.length*2);
			}
			b[cnt++] = (byte)read;		
		}
		result = new String(b);
		log.log(Level.INFO, result);
		s.close();			
		return result;		
	}

}
