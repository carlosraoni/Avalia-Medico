package hcpinforetriever.formatter;

import hcpinforetriever.model.Pharmacy;

import java.util.List;

public class PharmacysJSONFormatter implements PharmacysFormatter {

	@Override
	public String format(List<Pharmacy> pharmacys) {
		StringBuilder strBuilder = new StringBuilder();		
		int index = 0;
		
		strBuilder.append("[");
		strBuilder.append("\n");
		for(Pharmacy pharm: pharmacys){			
			strBuilder.append(toJSON(pharm) + ((index != pharmacys.size() - 1) ? "," : ""));
			strBuilder.append("\n");
			index++;
		}
		strBuilder.append("]");
		strBuilder.append("\n");
		
		return strBuilder.toString();
	}

	private String toJSON(Pharmacy pharm){		
		String json =
			  "{\"estado\":\"" + pharm.getState() + "\"" 
			+ ",\"cidade\":\"" + pharm.getCity() + "\"" 
			+ ",\"bairro\":\"" + pharm.getDistrict() + "\""
			+ ",\"nome\":\""	+ pharm.getName() + "\""
			+ ",\"endereco\":\"" + pharm.getAdress() + "\""
			+ ",\"telefone\":\"" + pharm.getPhone() + "\"}";

		return json;
	}
	
}
