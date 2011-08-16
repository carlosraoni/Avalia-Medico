package com.avaliamedico.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AvaliaMedicoServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(AvaliaMedicoServlet.class.getName());

	private static class BuscaAms {
		
		public String retrieveContent() {
			StringBuffer content = new StringBuffer();
			try {

				// Construct data
				String data = URLEncoder.encode("estado", "UTF-8") + "=" + URLEncoder.encode("CE", "UTF-8");
				data += "&" + URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("buscar", "UTF-8");

				// Send data
				//URL url = new URL("busca-ams.petrobras.com.br/buscaams/busca.do");
				URL url = new URL("http://busca-ams.petrobras.com.br/buscaams/busca.do");
				URLConnection conn = url.openConnection();				
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();				
				
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				//BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					content.append(line + "\n");
				}
				wr.close();
				rd.close();

			} catch (MalformedURLException e) {
				content.append(e.getMessage());
				logger.info(e.getMessage());
			} catch (IOException e) {
				content.append(e.getMessage());
				logger.info(e.getMessage());
			}

			return content.toString();
		}
		
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		resp.setContentType("text/html");

		BuscaAms busca = new BuscaAms();

		resp.getWriter().println(busca.retrieveContent());
		//resp.getWriter().println("Hello World!");
	}
}
