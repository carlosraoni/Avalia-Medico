package hcpinforetriever.formatter;

import hcpinforetriever.model.HealthCareProvider;

import java.util.List;


public interface HealthCareProvidersFormatter {
	String format(List<HealthCareProvider> providers);
}
