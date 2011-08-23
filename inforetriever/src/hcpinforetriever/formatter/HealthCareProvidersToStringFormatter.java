package hcpinforetriever.formatter;

import hcpinforetriever.model.HealthCareProvider;

import java.util.List;


public class HealthCareProvidersToStringFormatter implements HealthCareProvidersFormatter {
	
	@Override
	public String format(List<HealthCareProvider> providers) {
		StringBuilder strBuilder = new StringBuilder();
		for(HealthCareProvider prov: providers){
			strBuilder.append(prov);
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}

}
