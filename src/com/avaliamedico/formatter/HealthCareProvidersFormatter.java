package com.avaliamedico.formatter;

import java.util.List;

import com.avaliamedico.model.HealthCareProvider;

public interface HealthCareProvidersFormatter {
	String format(List<HealthCareProvider> providers);
}
