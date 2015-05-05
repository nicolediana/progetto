
public class Credenziali extends JsonObject {
	
	private Integer idcredenziali;
	private String email;
	private String password;
	private Integer idprofilo;
	
	public Credenziali(){}
	
	public Integer getIdcredenziali() {
		return idcredenziali;
	}
	public void setIdcredenziali(Integer idcredenziali) {
		this.idcredenziali = idcredenziali;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIdprofilo() {
		return idprofilo;
	}

	public void setIdprofilo(Integer idprofilo) {
		this.idprofilo = idprofilo;
	}
	

}
