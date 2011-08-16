package com.avaliamedico.formatter;

import java.util.List;

import com.avaliamedico.model.HealthCareProvider;

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
