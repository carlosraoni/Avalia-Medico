package com.avaliamedico.formatter;

import java.util.List;

import com.avaliamedico.model.HealthCareProvider;
import com.avaliamedico.model.HealthCareProvider.HealthCareProviderType;

public class HealthCareProvidersJSONFormatter implements HealthCareProvidersFormatter{

	@Override
	public String format(List<HealthCareProvider> providers) {
		StringBuilder strBuilder = new StringBuilder();		
		int index = 0;
		
		strBuilder.append("[");
		strBuilder.append("\n");
		for(HealthCareProvider prov: providers){			
			strBuilder.append(toJSON(prov) + ((index != providers.size() - 1) ? "," : ""));
			strBuilder.append("\n");
			index++;
		}
		strBuilder.append("]");
		strBuilder.append("\n");
		
		return strBuilder.toString();
	}

	private String toJSON(HealthCareProvider prov){		
		String json =
			  "{\"estado\":\"" + prov.getState() + "\"" 
			+ ",\"cidade\":\"" + prov.getCity() + "\""
			+ ",\"tipo\":\"" + ((prov.getType() == HealthCareProviderType.ENTITY) ? "PJ" : "PF") + "\"" 
			+ ",\"bairro\":\"" + prov.getDistrict() + "\""
			+ ",\"nome\":\""	+ prov.getName() + "\""
			+ ",\"endereco\":\"" + prov.getAdress() + "\""
			+ ",\"cep\":\"" + prov.getZipCode() + "\""
			+ ",\"telefone\":\"" + prov.getPhone() + "\""
			+ ",\"especialidades\":" + listToJSON(prov.getSpecialtys()) + "" 
			+ ",\"planos\":" + listToJSON(prov.getPlans()) + "}";

		return json;
	}

	private String listToJSON(List<String> list) {
		StringBuilder builder = new StringBuilder();
		
		int index = 0;
		builder.append("[");
		for(String str: list){
			builder.append("\"");
			builder.append(str);
			builder.append("\"");
			
			if(index != list.size() - 1){
				builder.append(",");
			}
			index++;
		}
		builder.append("]");
		
		return builder.toString();
	}
}
