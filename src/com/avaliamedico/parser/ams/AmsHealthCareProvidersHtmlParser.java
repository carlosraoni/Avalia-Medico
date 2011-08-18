package com.avaliamedico.parser.ams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.avaliamedico.extractor.HealthCareProvidersInformationExtractor;
import com.avaliamedico.extractor.ams.AmsFileHtmlInformationExtractor;
import com.avaliamedico.formatter.HealthCareProvidersFormatter;
import com.avaliamedico.formatter.HealthCareProvidersToStringFormatter;
import com.avaliamedico.model.HealthCareProvider;
import com.avaliamedico.model.HealthCareProvider.HealthCareProviderBuilder;
import com.avaliamedico.model.HealthCareProvider.HealthCareProviderType;
import com.avaliamedico.parser.HealthCareProvidersParser;

public class AmsHealthCareProvidersHtmlParser implements HealthCareProvidersParser{
	
	private static final String plan = "AMS";
	
	private String state;
	private String html;

	public AmsHealthCareProvidersHtmlParser(String state, String html){
		setState(state);
		setHtml(html);
	}
	
	@Override
	public List<HealthCareProvider> parse(){
		String html = preFilterHtml();
		
		Set<HealthCareProvider> providers = new TreeSet<HealthCareProvider>(HealthCareProvider.getComparator());
		
		String cityHeader = "Listando credenciados da cidade:";
		String space = "&nbsp;";
		String tableOpen = "<table";
		String tableClose = "</table>";
				
		int fromIndex = 0, nextIndex = 0;
		while((fromIndex = html.indexOf(cityHeader, fromIndex)) != -1){			
			fromIndex +=  cityHeader.length() + space.length();
			nextIndex = html.indexOf("<", fromIndex);
			String city = html.substring(fromIndex, nextIndex);			
			fromIndex = nextIndex;
			System.out.println(city);
			
			int indexTableTypeBegin = html.indexOf("<td", fromIndex);
			indexTableTypeBegin = html.indexOf(">", indexTableTypeBegin) + 1;
			int indexTableTypeEnd = html.indexOf("</td>", indexTableTypeBegin);
			String tableType = html.substring(indexTableTypeBegin, indexTableTypeEnd).trim();
			if(tableType.startsWith("Pessoas F")){
				// Parseando tabela de pessoa fisica
				fromIndex = html.indexOf(tableOpen, fromIndex);						
				nextIndex = html.indexOf(tableClose, fromIndex);
				parseTable(providers, city, html.substring(fromIndex, nextIndex), HealthCareProviderType.PERSON);
				fromIndex = nextIndex + tableClose.length();
				
				tableType = "";
				int indexType = html.indexOf("Pessoas J", fromIndex);
				if(indexType != -1){
					int cityIndex = html.indexOf(cityHeader, fromIndex);
					if(cityIndex == -1 || indexType < cityIndex){
						tableType = "Pessoas J";
					}
				}
			}
			if(tableType.startsWith("Pessoas J")){
				// Parseando tabela de pessoa jurídica
				fromIndex = html.indexOf(tableOpen, fromIndex);						
				nextIndex = html.indexOf(tableClose, fromIndex);
				parseTable(providers, city, html.substring(fromIndex, nextIndex), HealthCareProviderType.ENTITY);
				fromIndex = nextIndex + tableClose.length();
			}			
		}
				
		List<HealthCareProvider> provList = new ArrayList<HealthCareProvider>(providers);
		return provList;
	}
	
	private String preFilterHtml() {
		StringBuilder strBuilder = new StringBuilder(getHtml());		
		int indexBegin = 0, indexEnd = 0;
		String divNotaBegin = "<div id=\"nota";
		String divNotaEnd = "</div>";
		
		while((indexBegin = strBuilder.indexOf(divNotaBegin)) != -1){
			indexEnd = strBuilder.indexOf(divNotaEnd, indexBegin);
			indexEnd = strBuilder.indexOf(divNotaEnd, indexEnd + divNotaEnd.length()) + divNotaEnd.length();
			
			strBuilder.delete(indexBegin, indexEnd);		
		}
		
		return strBuilder.toString();
	}

	private void parseTable(Set<HealthCareProvider> providers, String city,	String table, HealthCareProviderType type) {
		String evenRowHeader = "<tr bgcolor=\"#FFFFFF\">";
		String oddRowHeader = "<tr bgcolor=\"#EEEEEE\">";
		
		parseRowsWithHeaderOf(providers, table, evenRowHeader, city, type);
		parseRowsWithHeaderOf(providers, table, oddRowHeader, city, type);
	}

	private void parseRowsWithHeaderOf(Set<HealthCareProvider> providers, String table, String header, String city, HealthCareProviderType type) {
		Pattern rowEvenPattern = Pattern.compile(header);
		Matcher tableMatcher = rowEvenPattern.matcher(table);
		while(tableMatcher.find()){
			int beginIndexRow = table.indexOf("<tr", tableMatcher.start());
			beginIndexRow = table.indexOf(">", beginIndexRow) + 1;
			int endIndexRow = table.indexOf("</tr>", beginIndexRow);
			
			String row = table.substring(beginIndexRow, endIndexRow).trim();
			HealthCareProvider newHealthProvider = parseFields(row, city, type);
			if(providers.contains(newHealthProvider)){
				// Bad implementation -> Java's Fault
				for(HealthCareProvider prov: providers){
					if(prov.equals(newHealthProvider)){
						prov.addSpectialtys(newHealthProvider.getSpecialtys());
						break;
					}
				}
			}
			else{
				providers.add(newHealthProvider);
			}					
		}
	}

	private HealthCareProvider parseFields(String row, String city, HealthCareProviderType type){
		HealthCareProviderBuilder builder = new HealthCareProviderBuilder();
		builder.city(city);
		builder.state(getState());
		builder.addPlan(plan);
		builder.type(type);
		
		Pattern fieldHeaderPattern = Pattern.compile("<td");
		Matcher fieldMatcher = fieldHeaderPattern.matcher(row);			
		int fieldIndex = 0;
		
		while(fieldMatcher.find()){
			int beginIndexField = row.indexOf("<td", fieldMatcher.start());
			beginIndexField = row.indexOf(">", beginIndexField) + 1;
			int endIndexField = row.indexOf("</td>", beginIndexField);
			
			String field = row.substring(beginIndexField, endIndexField).trim().replaceAll("\\s+", " ");
			switch (fieldIndex) {
			case 0:	builder.name(field); break;
			case 1:	builder.location(field); break;
			case 2:	builder.adress(field); break;
			case 3:	builder.zipCode(field); break;
			case 4:	builder.phone(field); break;
			case 5:	builder.addSpecialty(field); break;			
			}
			fieldIndex++;			
		}
		
		return builder.build();
	}

	private String getState() {
		return state;
	}

	private void setState(String state) {
		this.state = state;
	}
	
	private String getHtml() {
		return html;
	}

	private void setHtml(String html) {
		this.html = html;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String filePath = args[0];
		String state = args[1];
		
		HealthCareProvidersInformationExtractor extractor = new AmsFileHtmlInformationExtractor(filePath);
		String html = extractor.extractInfo();
		
		double iniTime = (double) System.currentTimeMillis();
		List<HealthCareProvider> providers = new AmsHealthCareProvidersHtmlParser(state, html).parse();		
		double endTime = (double) System.currentTimeMillis();
		double elapsedTime = (endTime - iniTime) / 1000.0;
		
		HealthCareProvidersFormatter formatter = new HealthCareProvidersToStringFormatter();
		//HealthCareProvidersFormatter formatter = new HealthCareProvidersJSONFormatter();
		
		System.out.println(formatter.format(providers));
		
		System.out.println("Number of Providers parsed: " + providers.size());
		System.out.println("Elapsed Time: " + elapsedTime);
	}


}
