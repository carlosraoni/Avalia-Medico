package hcpinforetriever.ams;

import hcpinforetriever.exception.AmsInfoRetrieveException;
import hcpinforetriever.extractor.ams.HealthCareProvidersAmsOnlineInformationExtractor;
import hcpinforetriever.formatter.HealthCareProvidersJSONFormatter;
import hcpinforetriever.model.BrazilState;
import hcpinforetriever.model.HealthCareProvider;
import hcpinforetriever.parser.ams.HealthCareProvidersAmsHtmlParser;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HealthCareProvidersAmsInfoRetriever {	
	
	private static final String amsFilePrefix = "ams-";
	private static final String jsonExtension = ".json";
	
	private void saveStringInFile(String str, String filePath) throws FileNotFoundException{
		PrintStream file = new PrintStream(filePath);
		file.println(str);
		file.close();				
	}
	
	private void retrieveAndSaveToJSONAmsInfo(List<BrazilState> states, String directoryPath) {
		
		String pathPrefix = directoryPath;
		if(!pathPrefix.endsWith("/")){
			pathPrefix += "/";
		}
		pathPrefix += amsFilePrefix;
		
		try{			
			System.out.println("#################### Obtendo informações dos credenciados da AMS ####################");			
			System.out.println("Data Início: " +  getStringPresentDate() +"\n");
			
			boolean firstState = true;
			for(BrazilState state: states){
			
				if(!firstState){
					System.out.println("Dormindo por 30 segundos.");					
					Thread.sleep(30000);
					System.out.println("-------------------------------------------------------------------------------------");
				}
								
				String filePath = pathPrefix + state;
				
				System.out.println("Requisitando informações dos credenciados da AMS do estado do " + state + " a partir do site: " + HealthCareProvidersAmsOnlineInformationExtractor.buscaAmsUrl);
				double iniTime = (double) System.currentTimeMillis();
				
				HealthCareProvidersAmsOnlineInformationExtractor extractor = new HealthCareProvidersAmsOnlineInformationExtractor(state.toString());								
				String html = extractor.extractInfo();
				
				double endTime = (double) System.currentTimeMillis();
				double elapsedTime = (endTime - iniTime) / 1000.0;				
				System.out.println("Tempo total de requisição "+ state.toString() + " = " + elapsedTime + "s");
				
				System.out.println("Parseando html recuperado ...");
				HealthCareProvidersAmsHtmlParser parser = new HealthCareProvidersAmsHtmlParser(state.toString(), html);
				List<HealthCareProvider> providers = parser.parse();
				
				System.out.println("Gerando arquivo JSON com as informações ...");
				HealthCareProvidersJSONFormatter JSONFormatter = new HealthCareProvidersJSONFormatter();							
				String json = JSONFormatter.format(providers);				
				filePath += jsonExtension;
				saveStringInFile(json, filePath);
				
				endTime = (double) System.currentTimeMillis();
				elapsedTime = (endTime - iniTime) / 1000.0;
				
				System.out.println("Informações da AMS do estado do " + state +" salvas com sucesso no arquivo: "+ filePath);
				System.out.println("Tempo total processando informações do estado do "+ state + " = " + elapsedTime + "s");
				
				firstState = false;
			}
		}catch(Throwable e){
			String msg = "Erro recuperando e/ou salvando informações da AMS: " + e.getMessage();
			throw new AmsInfoRetrieveException(msg, e);
		}
		
		System.out.println("\nData Fim: " +  getStringPresentDate());
		System.out.println("########################## Informações obtidas com sucesso ##########################");
	}
	
	private String getStringPresentDate() {
		Date data = new Date();
		Format formatter = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy - HH:mm:ss", new Locale("pt","BR"));

		return formatter.format(data);
	}

	public static void main(String[] args) {
		StringBuilder examples = new StringBuilder("Exemplos: \n");
		examples.append("\tRecuperando informações do CE e salvando no diretório de trabalho: java -jar 'ArquivoJarExecutavel' CE\n");
		examples.append("\tRecuperando informações de todos os estados e salvando no diretório de trabalho: java -jar 'ArquivoJarExecutavel' -a\n");
		examples.append("\tRecuperando informações do CE e salvando no diretório /home/username/amsinfo: java -jar 'ArquivoJarExecutavel' CE /home/username/amsinfo\n");
		examples.append("\tRecuperando informações de todos os estados e salvando no diretório /home/username/amsinfo: java -jar 'ArquivoJarExecutavel' -a /home/username/amsinfo\n");
				
		if(args == null || args.length < 1){
			System.out.println("É necessário especficar pelo menos o estado ou a opção -a.");
			System.out.println(examples);
			return;
		}
		if(args[0].equals("-h")){
			System.out.println(examples);
			return;
		}
		String stateOption = args[0];
		String directoryPath = ".";
		if(args.length > 1){
			directoryPath = args[1];			
		}
		try {
			List<BrazilState> states = null;
			if(stateOption.equalsIgnoreCase("-a")){
				states = Arrays.asList(BrazilState.values());
			}
			else{
				states = new ArrayList<BrazilState>();
				states.add(BrazilState.valueOf(stateOption));				
			}
			
			HealthCareProvidersAmsInfoRetriever amsInfoRetriever = new HealthCareProvidersAmsInfoRetriever();
			amsInfoRetriever.retrieveAndSaveToJSONAmsInfo(states, directoryPath);
			
		} catch (IllegalArgumentException e) {
			System.out.println(stateOption + " não representa uma opção válida de estado." );
			System.out.println("Use a opção -a para recuperar as informações de todos os estados ou especifique uma das opções de estados abaixo: ");
			System.out.println(Arrays.asList(BrazilState.values()));
			System.out.println("Para mais informações utilize a opção -h");
		} catch (Throwable e) {
			System.out.println("Erro inesperado!");
			e.printStackTrace();
		}
	}

}
