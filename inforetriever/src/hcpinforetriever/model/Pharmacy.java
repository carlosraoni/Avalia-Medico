package hcpinforetriever.model;


public class Pharmacy {
	
	private String state;
	private String city;
	private String district;
	private String name;
	private String adress;
	private String phone;	
		
	public Pharmacy(String state, String city, String district, String name, String adress, String phone) {
		this.state = state;
		this.city = city;
		this.district = district;
		this.name = name;
		this.adress = adress;
		this.phone = phone;
	}

	public static final class PharmacyBuilder{
		
		private String state;
		private String city;
		private String district;
		private String name;
		private String adress;
		private String phone;
		
		public PharmacyBuilder() {
		}
		
		public Pharmacy build(){
			if(checkFields())
				throw new IllegalArgumentException("Build Error: some fields are null.");
			return new Pharmacy(getState(), getCity(), getDistrict(), getName(), getAdress(), getPhone());
		}
		
		private boolean checkFields() {			
			return 
					getName() == null || 
					getDistrict() == null || 
					getAdress() == null || 
					getPhone() == null || 
					getCity() == null || 
					getState() == null;
		}

		private String getName() {
			return name;
		}
		private void setName(String name) {
			this.name = name;
		}
		public PharmacyBuilder name(String name) {
			setName(name);
			return this;			
		}
		
		private String getDistrict() {
			return district;
		}
		private void setDistrict(String district) {
			this.district = district;
		}
		public PharmacyBuilder district(String district) {
			setDistrict(district);
			return this;			
		}
		
		private String getAdress() {
			return adress;
		}
		private void setAdress(String adress) {
			this.adress = adress;
		}
		public PharmacyBuilder adress(String adress) {
			setAdress(adress);
			return this;			
		}
		
		private String getPhone() {
			return phone;
		}
		private void setPhone(String phone) {
			this.phone = phone;
		}
		public PharmacyBuilder phone(String phone) {
			setPhone(phone);
			return this;			
		}
		
		private String getCity() {
			return city;
		}
		private void setCity(String city) {
			this.city = city;
		}
		public PharmacyBuilder city(String city) {
			setCity(city);
			return this;			
		}
		
		public String getState() {
			return state;
		}
		private void setState(String state) {
			this.state = state;
		}
		public PharmacyBuilder state(String state) {
			setState(state);
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


	public String getCity() {
		return city;
	}

	
	public String getState() {
		return state;
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
		Pharmacy other = (Pharmacy) obj;
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
		return true;
	}


}
