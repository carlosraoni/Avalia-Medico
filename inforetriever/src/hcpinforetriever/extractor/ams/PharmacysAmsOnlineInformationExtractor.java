package hcpinforetriever.extractor.ams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import hcpinforetriever.exception.ExtractInformationException;
import hcpinforetriever.extractor.PharmacysInformationExtractor;
import hcpinforetriever.model.BrazilState;

public class PharmacysAmsOnlineInformationExtractor implements
		PharmacysInformationExtractor {

	public static final String buscaFarmaciaAmsUrl = "http://buscafarm-ams.petrobras.com.br/Forms/conFarmacia.aspx";
	
	private String state;
	
	public PharmacysAmsOnlineInformationExtractor(String state){
		setState(state);
	}
	
	private String getEncodedRequestParameters() throws UnsupportedEncodingException {				
		String data = "__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwULLTE3MTEzNjUyNjUPZBYCZg9kFggCAw8WAh4HVmlzaWJsZWhkAgUPFgIfAGhkAgsPDxYCHgRUZXh0BRRGYXJtw6FjaWEgLSBDb25zdWx0YWRkAg0PZBYEAgEPZBYIAgMPEA8WBh4NRGF0YVRleHRGaWVsZAUETm9tZR4ORGF0YVZhbHVlRmllbGQFBVNpZ2xhHgtfIURhdGFCb3VuZGdkEBUcEUVzY29saGEgdW0gZXN0YWRvBEFDUkUHQUxBR09BUwhBTUFaT05BUwVBTUFQQQVCQUhJQQVDRUFSQRBESVNUUklUTyBGRURFUkFMDkVTUElSSVRPIFNBTlRPBUdPSUFTCE1BUkFOSEFPDE1JTkFTIEdFUkFJUxJNQVRPIEdST1NTTyBETyBTVUwLTUFUTyBHUk9TU08EUEFSQQdQQVJBSUJBClBFUk5BTUJVQ08FUElBVUkGUEFSQU5BDlJJTyBERSBKQU5FSVJPE1JJTyBHUkFOREUgRE8gTk9SVEUIUk9ORE9OSUEHUk9SQUlNQRFSSU8gR1JBTkRFIERPIFNVTA5TQU5UQSBDQVRBUklOQQdTRVJHSVBFCVNBTyBQQVVMTwlUT0NBTlRJTlMVHAACQUMCQUwCQU0CQVACQkECQ0UCREYCRVMCR08CTUECTUcCTVMCTVQCUEECUEICUEUCUEkCUFICUkoCUk4CUk8CUlICUlMCU0MCU0UCU1ACVE8UKwMcZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZxYBAhNkAgkPEA8WCB8CBQROb21lHwMFAklkHwRnHgdFbmFibGVkZ2QQFSgQVG9kYXMgYXMgY2lkYWRlcw5BTkdSQSBET1MgUkVJUwhBUkFSVUFNQRJBUk1BQ0FPIERPUyBCVVpJT1MPQVJSQUlBTCBETyBDQUJPDkJBUlJBIERPIFBJUkFJC0JBUlJBIE1BTlNBDEJFTEZPUkQgUk9YTwlDQUJPIEZSSU8VQ0FNUE9TIERPUyBHT1lUQUNBWkVTE0NPTkNFSUNBTyBERSBNQUNBQlUPRFVRVUUgREUgQ0FYSUFTCElUQUJPUkFJB0lUQUdVQUkJSVRBUEVSVU5BBU1BQ0FFBE1BR0UGTUFSSUNBCE1FU1FVSVRBCU5JTE9QT0xJUwdOSVRFUk9JDU5PVkEgRlJJQlVSR08LTk9WQSBJR1VBQ1UJUEFSQUNBTUJJDlBBUkFJQkEgRE8gU1VMClBFVFJPUE9MSVMHUkVTRU5ERQ5SSU8gREFTIE9TVFJBUw5SSU8gREUgSkFORUlSTwtTQU8gRklERUxJUwtTQU8gR09OQ0FMTxFTQU8gSk9BTyBEQSBCQVJSQRJTQU8gSk9BTyBERSBNRVJJVEkTU0FPIFBFRFJPIERBIEFMREVJQQlTQVFVQVJFTUELVEVSRVNPUE9MSVMJVFJFUyBSSU9TB1ZBTEVOQ0EJVkFTU09VUkFTDVZPTFRBIFJFRE9OREEVKAAFNzU5MzgFNzU5NDAFNzU5NDIFNzU5NDMFNzU5NDQFNzU5NDUFNzU5NDYFNzU5NDkFNzU5NTIFNzU5NTkFNzU5NjIFNzU5NjYFNzU5NjcFNzU5NzAFNzU5NzQFNzU5NzYFNzU5NzgFNzU5ODAFNzU5ODQFNzU5ODUFNzU5ODYFNzU5ODcFNzU5ODgFNzU5ODkFNzU5OTIFNzYwMDAFNzYwMDQFNzYwMDUFNzYwMDgFNzYwMTAFNzYwMTEFNzYwMTIFNzYwMTUFNzYwMTgFNzYwMjMFNzYwMjUFNzYwMjYFNzYwMjgFNzYwMjkUKwMoZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZxYBZmQCDQ8QZBAVATDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqAVATDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgwqAUKwMBZxYAZAIZDw8WBB8BBRFJbmNsdWlyIEZhcm3DoWNpYR8AaGRkAgUPZBYCZg9kFgJmD2QWBgIBDw8WBB8BBQ9Qw6FnaW5hIEluaWNpYWwfAGhkZAIDDw8WBB8BBQMgfCAfAGhkZAIFDw8WBB8BBQRTYWlyHwBoZGRkh0QY5HHScBhGySTWO%2FLWqgSvLio%3D&__EVENTVALIDATION=%2FwEWTAKulvSvAgKY3uX3DwKY3uX3DwLHsfuHAwLHsd%2BHAwLHsdOHAwLHsY%2BHAwLGscOHAwLFsfOHAwLEsfeHAwLDsbuHAwLBscuHAwL7scOHAwL7seuHAwL7sbuHAwL7sb%2BHAwLoscOHAwLosceHAwLosfOHAwLosaOHAwLosYeHAwL2saeHAwL2sdeHAwL2scuHAwL2sYeHAwL2sbuHAwL1sfuHAwL1sfOHAwL1sY%2BHAwL0scuHAwKy1Z7mAwKy1Z7mAwKm55mSCgK%2B5%2B3%2BAgK45%2B3%2BAgK55%2B3%2BAgK65%2B3%2BAgK75%2B3%2BAgK05%2B3%2BAgKn5%2B3%2BAgK458HbCwKn58HbCwK459WEDAK059WEDAK159WEDAK%2B56nhBAK656nhBAK056nhBAKm56nhBAK%2B5%2F24CgK65%2F24CgK75%2F24CgK05%2F24CgK15%2F24CgKm5%2F24CgKn5%2F24CgK459HlAgL7x47%2FCAL3x47%2FCAL0x47%2FCALjx47%2FCAL7x%2BLbAQL4x%2BLbAQL1x%2BLbAQL0x%2BLbAQLjx%2BLbAQL6x%2FaECgL0x%2FaECgLxx%2FaECgLjx%2FaECgLgx%2FaECgLyr6vlDwLljfv7CQLr%2FbqBCQLmvP3JCgLJsfC%2FBqjdeTOt52%2FhfTvGYhwaQ1Cln168&ctl00%24cphPrincipal%24ddlEstados="+getState()+"&ctl00%24cphPrincipal%24ddlCidades=&ctl00%24cphPrincipal%24txtFarmacia=&ctl00%24cphPrincipal%24btnConsultar=Consultar";
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

			url = new URL(buscaFarmaciaAmsUrl);
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
	
	public static void main(String [] args){
		PharmacysAmsOnlineInformationExtractor extractor = new PharmacysAmsOnlineInformationExtractor(BrazilState.CE.toString());
		
		System.out.println(extractor.extractInfo());		
	}
}
