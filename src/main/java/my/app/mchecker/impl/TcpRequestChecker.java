package my.app.mchecker.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.mchecker.Checker;
import my.app.mchecker.impl.ClaymoreChecker.Response;

public class TcpRequestChecker {

	public static final int TIMEOUT = 20000;
	public static final String HEADER_MAIN = "HTTP";
	public static final String HEADER_CL = "Content-Length:";
	public static final String LFLF = "\n\n";
	public static final String LFCRLF = "\n\r\n";
	public static final Pattern RE_CL = Pattern.compile(HEADER_CL + "\\s*\\d+");
	
	private static final Logger log = Logger.getLogger(TcpRequestChecker.class.getName());
	
	/**
	 * @param host
	 * @param port
	 * @param request
	 * @return response 
	 */
	protected String sendRequest(String host, int port, String request) throws IOException {
		StringBuilder sb = new StringBuilder();
		Socket s = new Socket(host, port);
		s.setSoTimeout(TIMEOUT);
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();
		os.write(request.getBytes());
		os.flush();
		int read = 0;
		int cnt = 0;
		int contentLength = 0;
		boolean isHttp = false;
		boolean isHttpHeaderPassed = false;
		while ((read = is.read()) >= 0) { 
			sb.append((char)read);
			// if content is HTTP, then read only 'Content-Length' bytes
			if (isHttp) {
				if (!isHttpHeaderPassed) {
					// looking for the header block to pass
					if (sb.indexOf(LFLF) >= 0 || sb.indexOf(LFCRLF) >= 0) {
						isHttpHeaderPassed = true;
						// trying to parse the 'Content-Length:'
						Matcher m = RE_CL.matcher(sb);
						if (m.find()) {
							String line = m.group().replaceFirst(HEADER_CL, "").trim();
							contentLength = Integer.valueOf(line);
							sb.delete(0, sb.length());
						}												
					}
				} else {
					cnt++;
					if (cnt == contentLength) {
						break;
					}
				}
			} else {
				isHttp = sb.indexOf(HEADER_MAIN) >= 0 ? true : false; 
			}			
		}
		log.log(Level.FINE, sb.toString());
		s.close();			
		return sb.toString();		
	}

}
