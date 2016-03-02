
public class Partita {
	
	private Integer idpartita;
	private Integer idtipopartita;
	private String nomecampo;
	private String indirizzocampo;
	private String data;
	private String citta;
	private String provincia;
	private Float costo;	
	private String linkfotocampo;
	private String terreno;
	private String coperto;
	private String note;
	private Integer amministratore;
	private String contatto;
	
	public Partita(){}
	
	public Integer getIdpartita() {
		return idpartita;
	}
	public void setIdpartita(Integer idpartita) {
		this.idpartita = idpartita;
	}
	public String getNomecampo() {
		return nomecampo;
	}
	public void setNomecampo(String nomecampo) {
		this.nomecampo = nomecampo;
	}
	public String getIndirizzocampo() {
		return indirizzocampo;
	}
	public void setIndirizzocampo(String indirizzocampo) {
		this.indirizzocampo = indirizzocampo;
	}
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public Float getCosto() {
		return costo;
	}
	public void setCosto(Float costo) {
		this.costo = costo;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public Integer getIdtipopartita() {
		return idtipopartita;
	}

	public void setIdtipopartita(Integer idtipopartita) {
		this.idtipopartita = idtipopartita;
	}

	public String getLinkfotocampo() {
		return linkfotocampo;
	}

	public void setLinkfotocampo(String linkfotocampo) {
		this.linkfotocampo = linkfotocampo;
	}

	public String getTerreno() {
		return terreno;
	}

	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}

	public String getCoperto() {
		return coperto;
	}

	public void setCoperto(String coperto) {
		this.coperto = coperto;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getAmministratore() {
		return amministratore;
	}

	public void setAmministratore(Integer amministratore) {
		this.amministratore = amministratore;
	}

	public String getContatto() {
		return contatto;
	}

	public void setContatto(String contatto) {
		this.contatto = contatto;
	}

}