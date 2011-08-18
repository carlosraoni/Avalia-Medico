package com.avaliamedico;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import com.avaliamedico.extractor.ams.AmsInternetHtmlInformationExtractor;
import com.avaliamedico.formatter.HealthCareProvidersJSONFormatter;
import com.avaliamedico.formatter.HealthCareProvidersToStringFormatter;
import com.avaliamedico.model.HealthCareProvider;
import com.avaliamedico.model.BrazilState;
import com.avaliamedico.parser.ams.AmsHealthCareProvidersHtmlParser;

public class AmsInfoRetriever {

	private static void saveStringInFile(String str, String filePath){
		try {
			PrintStream file = new PrintStream(filePath);
			file.println(str);
			file.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Obtendo as informações da AMS ---------------------");
		for(BrazilState state: BrazilState.values()){
			AmsInternetHtmlInformationExtractor extractor = new AmsInternetHtmlInformationExtractor(state.toString());
			String filePath = "/home/raoni/Downloads/ams/ams-" + state;
			System.out.println("Obtendo Informações do estado: " + state);
			double iniTime = (double) System.currentTimeMillis();
			
			String html = extractor.extractInfo();
			saveStringInFile(html, filePath + ".html");
			
			AmsHealthCareProvidersHtmlParser parser = new AmsHealthCareProvidersHtmlParser(state.toString(), html);
			List<HealthCareProvider> providers = parser.parse();
			
			HealthCareProvidersJSONFormatter JSONFormatter = new HealthCareProvidersJSONFormatter();
			HealthCareProvidersToStringFormatter txtFormatter = new HealthCareProvidersToStringFormatter();
						
			String json = JSONFormatter.format(providers);
			saveStringInFile(json, filePath + ".json");
			String txt = txtFormatter.format(providers);
			saveStringInFile(txt, filePath + ".txt");
			
			double endTime = (double) System.currentTimeMillis();
			double elapsedTime = (endTime - iniTime) / 1000.0;
			
			System.out.println("Informações do " + state +" salvas com sucesso.");
			System.out.println("Tempo total processando "+ state.toString() + " = " + elapsedTime);
			System.out.println("Dormindo por 30 segundos.");
			
			Thread.sleep(30000);
		}
		System.out.println("Informações Obtidas com sucesso ---------------------");
	}
}
