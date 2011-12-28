package hcpinforetriever.extractor.ams;

import hcpinforetriever.exception.ExtractInformationException;
import hcpinforetriever.extractor.HealthCareProvidersInformationExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class HealthCareProvidersAmsOnlineInformationExtractor implements	HealthCareProvidersInformationExtractor {

	public static final String buscaAmsUrl = "http://busca-ams.petrobras.com.br/buscaams/busca.do";
	
	private String state;
	
	public HealthCareProvidersAmsOnlineInformationExtractor(String state){
		setState(state);
	}
	
	private String getEncodedRequestParameters() throws UnsupportedEncodingException {
		String data = URLEncoder.encode("estado", "UTF-8") + "=" + URLEncoder.encode(getState(), "UTF-8");
		data += "&" + URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("buscar", "UTF-8");
		return data;
	}
	
	@Override
	public String extractInfo() {
		URL url = null;
		URLConnection conn = null;
		OutputStreamWriter wr = null;
		BufferedReader rd = null;
		
		StringBuffer content = new StringBuffer();
		try {
			// Obtem os parametros a serem submetidos no request
			String data = getEncodedRequestParameters();

			url = new URL(buscaAmsUrl);
			conn = url.openConnection();				
			conn.setDoOutput(true);
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();				
			
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				content.append(line);
				content.append("\n");
			}
			wr.close();
			rd.close();

		} catch (MalformedURLException e) {
			throw new ExtractInformationException(e);
		} catch (IOException e) {			
			throw new ExtractInformationException(e);
		}
		finally{
			try{
				if(wr != null){
					wr.close();
				}
				if(rd != null){
					rd.close();
				}
			}catch(IOException e){
				throw new ExtractInformationException(e);
			}
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
