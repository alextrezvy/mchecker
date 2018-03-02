package my.app.mchecker.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import my.app.mchecker.Checker;

public class XMRStakChecker extends TcpRequestChecker implements Checker {
	
	private static final Logger log = Logger.getLogger(XMRStakChecker.class.getName());
	private Pattern RE = Pattern.compile("<th>Totals:</th><td>(\\s*[0-9]*\\.?[0-9]*)</td>");
	
	private String host;
	private int port;
	private String request = "GET /h HTTP/1.1\r\nHost: 127.0.0.1\r\n\r\n";
	
	
	public XMRStakChecker(String host, int port, String request) {
		this.host = host;
		this.port = port;
		this.request = (request != null && !request.isEmpty() ? request : this.request);
	}

	@Override
	public boolean check() {
		boolean result = true;
		try {
			String response = sendRequest(host, port, request);
			if (response.isEmpty()) {
				result = false;
			} else {
				Matcher m = RE.matcher(response);
				if (m.find())
					log.log(Level.INFO, "Total hashrate: {0}", m.group(1));
			}	
		} catch (IOException e) {
			log.log(Level.INFO, e.getMessage());
			result = false;
		}
		return result;
	}
	
}
