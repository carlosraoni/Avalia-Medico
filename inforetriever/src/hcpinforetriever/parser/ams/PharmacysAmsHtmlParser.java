package hcpinforetriever.parser.ams;

import hcpinforetriever.extractor.PharmacysInformationExtractor;
import hcpinforetriever.extractor.ams.PharmacysAmsOnlineInformationExtractor;
import hcpinforetriever.formatter.PharmacysFormatter;
import hcpinforetriever.formatter.PharmacysJSONFormatter;
import hcpinforetriever.model.BrazilState;
import hcpinforetriever.model.Pharmacy;
import hcpinforetriever.model.Pharmacy.PharmacyBuilder;
import hcpinforetriever.parser.PharmacysParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PharmacysAmsHtmlParser implements PharmacysParser {

	private String state;
	private String html;
	
	public PharmacysAmsHtmlParser(String state, String html) {
		this.state = state;
		this.html = html;
	}
	
	@Override
	public List<Pharmacy> parse() {
		List<Pharmacy> pharmacys = new ArrayList<Pharmacy>();
		
		String cityHeader = "Listando farmácias da cidade:";
		String tableOpen = "<table";
		String tableClose = "</table>";
		
		int fromIndex = 0, nextIndex = 0;
		while((fromIndex = html.indexOf(cityHeader, fromIndex)) != -1){			
			fromIndex = html.indexOf(">", fromIndex) + 1;
			nextIndex = html.indexOf("<", fromIndex);
			String city = html.substring(fromIndex, nextIndex);			
			fromIndex = nextIndex;
			System.out.println(city);
			
			// Parseando tabela de fármacias
			fromIndex = html.indexOf(tableOpen, fromIndex);						
			nextIndex = html.indexOf(tableClose, fromIndex);
			parseTable(pharmacys, city, html.substring(fromIndex, nextIndex));
			fromIndex = nextIndex + tableClose.length();
		}
				
		return pharmacys;
	}

	private void parseTable(List<Pharmacy> pharmacys, String city, String table) {
		String tableRowBegin = "<tr";
		String tableRowEnd = "</tr>";		
		
		Pattern rowPattern = Pattern.compile(tableRowBegin);
		Matcher tableMatcher = rowPattern.matcher(table);
		
		// Pular linha de título da tabela
		tableMatcher.find();
		
		while(tableMatcher.find()){
			int beginIndexRow = table.indexOf(tableRowBegin, tableMatcher.start());
			beginIndexRow = table.indexOf(">", beginIndexRow) + 1;
			int endIndexRow = table.indexOf(tableRowEnd, beginIndexRow);
			
			String row = table.substring(beginIndexRow, endIndexRow).trim();
			Pharmacy newPharm = parseFields(row, city);
								
			pharmacys.add(newPharm);
		}
	}

	private Pharmacy parseFields(String row, String city) {
		String tableColBegin = "<td";
		String tableColEnd = "</td>";
		
		PharmacyBuilder builder = new PharmacyBuilder();
		builder.city(city);
		builder.state(getState());
		
		Pattern fieldHeaderPattern = Pattern.compile(tableColBegin);
		Matcher fieldMatcher = fieldHeaderPattern.matcher(row);			
		int fieldIndex = 0;
		
		while(fieldMatcher.find()){
			int beginIndexField = row.indexOf(tableColBegin, fieldMatcher.start());
			beginIndexField = row.indexOf(">", beginIndexField) + 1;
			int endIndexField = row.indexOf(tableColEnd, beginIndexField);
			
			String field = row.substring(beginIndexField, endIndexField).trim().replaceAll("\\s+", " ");
			switch (fieldIndex) {
			case 0:	builder.name(field); break;
			case 1:	builder.adress(field); break;
			case 2:	builder.district(field); break;
			case 3:	builder.phone(field); break;			
			}
			fieldIndex++;			
		}
		
		return builder.build();
	}

	public String getState() {
		return state;
	}

	public String getHtml() {
		return html;
	}
	
	public static void main(String [] args){
		String state = BrazilState.RJ.toString();
		PharmacysInformationExtractor extractor = new PharmacysAmsOnlineInformationExtractor(state);
		PharmacysParser parser = new PharmacysAmsHtmlParser(state, extractor.extractInfo());
		PharmacysFormatter formatter = new PharmacysJSONFormatter();
		
		List<Pharmacy> pharmacys = parser.parse();
		System.out.println(formatter.format(pharmacys));		
		System.out.println("Total de Fármacias: " + pharmacys.size());
	}
}
