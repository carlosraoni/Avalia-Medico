package com.avaliamedico.extractor.ams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.avaliamedico.extractor.HealthCareProvidersInformationExtractor;

public class AmsInternetHtmlInformationExtractor implements	HealthCareProvidersInformationExtractor {

	private static final String buscaAmsUrl = "http://busca-ams.petrobras.com.br/buscaams/busca.do";
	
	private String state;
	
	public AmsInternetHtmlInformationExtractor(String state){
		setState(state);
	}
	
	@Override
	public String extractInfo() {
		StringBuffer content = new StringBuffer();
		try {
			// Construct data
			String data = URLEncoder.encode("estado", "UTF-8") + "=" + URLEncoder.encode(getState(), "UTF-8");
			data += "&" + URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("buscar", "UTF-8");

			URL url = new URL(buscaAmsUrl);
			URLConnection conn = url.openConnection();				
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();				
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				content.append(line + "\n");
			}
			wr.close();
			rd.close();

		} catch (MalformedURLException e) {			
		} catch (IOException e) {			
		}

		return content.toString();
	}

	private String getState() {
		return state;
	}

	private void setState(String state) {
		this.state = state;
	}

}
