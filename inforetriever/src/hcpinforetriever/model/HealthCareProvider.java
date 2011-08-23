package hcpinforetriever.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HealthCareProvider{

	public enum HealthCareProviderType{
		PERSON, ENTITY
	}
	
	private String state;
	private String city;
	private HealthCareProviderType type;
	private String district;
	private String name;
	private String adress;
	private String zipCode;
	private String phone;	
	private List<String> specialtys;
	private List<String> plans;
		
	private HealthCareProvider(String state, String city, HealthCareProviderType type, String district, String name, String adress, String zipCode, String phone, List<String> specialtys, List<String> plans) {
		this.state = state;
		this.type = type;
		this.name = name;
		this.district = district;
		this.adress = adress;
		this.phone = phone;
		this.zipCode = zipCode;
		this.city = city;
		this.specialtys = specialtys;
		this.plans = plans;
	}

	public static final class HealthCareProviderBuilder{
		private String state;
		private String city;
		private HealthCareProviderType type;
		private String district;
		private String name;
		private String adress;
		private String zipCode;
		private String phone;	
		private List<String> specialtys;
		private List<String> plans;
		
		public HealthCareProviderBuilder(){}
		
		public HealthCareProvider build(){
			if(checkFields())
				throw new IllegalArgumentException("Build Error: some fields are null.");
			return new HealthCareProvider(getState(), getCity(), getType(), getDistrict(), getName(), getAdress(), getZipCode(), getPhone(), getSpecialtys(), getPlans());
		}
		
		private boolean checkFields() {			
			return 
					getName() == null || 
					getDistrict() == null || 
					getAdress() == null || 
					getPhone() == null || 
					getZipCode() == null || 
					getCity() == null || 
					getSpecialtys() == null ||
					getState() == null ||
					getType() == null ||
					getPlans() == null;
		}

		private String getName() {
			return name;
		}
		private void setName(String name) {
			this.name = name;
		}
		public HealthCareProviderBuilder name(String name) {
			setName(name);
			return this;			
		}
		
		private String getDistrict() {
			return district;
		}
		private void setDistrict(String district) {
			this.district = district;
		}
		public HealthCareProviderBuilder location(String district) {
			setDistrict(district);
			return this;			
		}
		
		private String getAdress() {
			return adress;
		}
		private void setAdress(String adress) {
			this.adress = adress;
		}
		public HealthCareProviderBuilder adress(String adress) {
			setAdress(adress);
			return this;			
		}
		
		private String getPhone() {
			return phone;
		}
		private void setPhone(String phone) {
			this.phone = phone;
		}
		public HealthCareProviderBuilder phone(String phone) {
			setPhone(phone);
			return this;			
		}
		
		private String getZipCode() {
			return zipCode;
		}
		private void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}
		public HealthCareProviderBuilder zipCode(String zipCode) {
			setZipCode(zipCode);
			return this;			
		}
		
		
		private String getCity() {
			return city;
		}
		private void setCity(String city) {
			this.city = city;
		}
		public HealthCareProviderBuilder city(String city) {
			setCity(city);
			return this;			
		}
		
		private List<String> getSpecialtys() {
			return specialtys;
		}
		
		public HealthCareProviderBuilder addSpecialty(String specialty) {
			if(specialtys == null){
				specialtys = new ArrayList<String>();
			}
			specialtys.add(specialty);
			return this;			
		}
		
		public String getState() {
			return state;
		}
		private void setState(String state) {
			this.state = state;
		}
		public HealthCareProviderBuilder state(String state) {
			setState(state);
			return this;
		}

		public HealthCareProviderType getType() {
			return type;
		}
		public void setType(HealthCareProviderType type) {
			this.type = type;
		}
		public HealthCareProviderBuilder type(HealthCareProviderType type) {
			setType(type);
			return this;
		}
	
		public List<String> getPlans() {
			return plans;
		}
		public HealthCareProviderBuilder addPlan(String plan) {
			if(plans == null){
				plans = new ArrayList<String>();
			}
			plans.add(plan);
			return this;			
		}


	}

	public String getName() {
		return name;
	}

	public String getDistrict() {
		return district;
	}

	public String getAdress() {
		return adress;
	}

	public String getPhone() {
		return phone;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public List<String> getSpecialtys() {
		return specialtys;
	}

	public void addSpectialty(String spec){
		specialtys.add(spec);
	}
		
	public void addSpectialtys(List<String> specs){
		for(String spec: specs)
			addSpectialty(spec);
	}
	
	public String getState() {
		return state;
	}

	public HealthCareProviderType getType() {
		return type;
	}

	public List<String> getPlans() {
		return plans;
	}

	@Override
	public String toString() {
		return "[estado=\"" + state + "\"" 
				+ ", cidade=\"" + city + "\""
				+ ", tipo=\"" + ((type == HealthCareProviderType.ENTITY) ? "PJ" : "PF") + "\"" 
				+ ", bairro=\"" + district + "\""
				+ ", nome=\""	+ name + "\""
				+ ", endereco=\"" + adress + "\""
				+ ", cep=\"" + zipCode + "\""
				+ ", telefone=\"" + phone + "\""
				+ ", especialidades=" + specialtys
				+ ", planos=" + plans + "]";
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adress == null) ? 0 : adress.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((district == null) ? 0 : district.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HealthCareProvider other = (HealthCareProvider) obj;
		if (adress == null) {
			if (other.adress != null)
				return false;
		} else if (!adress.equals(other.adress))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (district == null) {
			if (other.district != null)
				return false;
		} else if (!district.equals(other.district))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	private static HealthCareProviderComparator comparatorInstance = new HealthCareProviderComparator();
	
	public static HealthCareProviderComparator getComparator(){
		return comparatorInstance;
	}
	
	private static final class HealthCareProviderComparator implements Comparator<HealthCareProvider>{		
		@Override
		public int compare(HealthCareProvider o1, HealthCareProvider o2) {
			int compState = o1.getState().compareToIgnoreCase(o2.getState());
			if(compState != 0)
				return compState;			
			int compCity = o1.getCity().compareToIgnoreCase(o2.getCity());
			if(compCity != 0)
				return compCity;
			int compType = o1.getType().compareTo(o2.getType());
			if(compType != 0)
				return compType;			
			int compDistrict = o1.getDistrict().compareToIgnoreCase(o2.getDistrict());
			if(compDistrict != 0)
				return compDistrict;
			int compName = o1.getName().compareToIgnoreCase(o2.getName());
			if(compName != 0)
				return compName;		
			int compAdress = o1.getAdress().compareToIgnoreCase(o2.getAdress());
			if(compAdress != 0)
				return compAdress;
			
			return 0;
		}		
	}
	
}
