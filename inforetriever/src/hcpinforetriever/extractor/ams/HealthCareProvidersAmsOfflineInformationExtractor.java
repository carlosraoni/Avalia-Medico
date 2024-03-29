package hcpinforetriever.extractor.ams;

import hcpinforetriever.exception.ExtractInformationException;
import hcpinforetriever.extractor.HealthCareProvidersInformationExtractor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class HealthCareProvidersAmsOfflineInformationExtractor implements HealthCareProvidersInformationExtractor {

	private String filePath;
	
	public HealthCareProvidersAmsOfflineInformationExtractor(String filePath) {		
		setFilePath(filePath);
	}

	@Override
	public String extractInfo() {
		FileInputStream fis = null;
		BufferedReader reader = null;
		StringBuilder strBuilder = null;
		
		try{
			fis = new FileInputStream(getFilePath());
			reader = new BufferedReader(new InputStreamReader(fis));
			
			strBuilder = new StringBuilder();
			String line = reader.readLine();
			strBuilder.append(line);
			strBuilder.append("\n");
			
			while(line != null){
				line = reader.readLine();
				if(line != null){
					strBuilder.append(line);
					strBuilder.append("\n");
				}
			}
			
		} catch (FileNotFoundException e) {			
			throw new ExtractInformationException(e);
		} catch (IOException e) {
			throw new ExtractInformationException(e);
		} finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					throw new ExtractInformationException(e);
				}
			}
		}
		
		return strBuilder.toString();
	}
	
	private String getFilePath() {
		return filePath;
	}

	private void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
