import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Geisternetz implements Serializable {
	private static final long serialVersionUID =1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nr;

    private String standort;
    private double lat; 
    private double lng; 
    private String groeße;
    private String bild;
    @Enumerated(EnumType.STRING)
    private NetzStatus netzStatus;
    
    
    
    @OneToMany(mappedBy = "geisternetz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Meldung> meldungen = new ArrayList<>();
    
    @OneToMany(mappedBy = "geisternetz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bergung> bergungsAnmeldungen = new ArrayList<>();
    
    @OneToMany(mappedBy = "geisternetz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Verschollen> verschollenMeldungen = new ArrayList<>();
    
    
    
    
    
    public Geisternetz() {}

    public Geisternetz( String standort, double lat, double lng, String groeße, NetzStatus netzStatus, String bild)
    {
        this.setStandort(standort);
        this.lat = lat;
        this.lng = lng;
        this.setGroeße(groeße);
        this.setNetzStatus(netzStatus);
        this.setBild(bild);

    }
    
    
    
    // Getter und Setter für Beziehungen
    public List<Meldung> getMeldungen() { return meldungen; }
    public void setMeldungen(List<Meldung> meldungen) { this.meldungen = meldungen; }
    
    public List<Bergung> getBergungsAnmeldungen() { return bergungsAnmeldungen; }
    public void setBergungsAnmeldungen(List<Bergung> bergungsAnmeldungen) { this.bergungsAnmeldungen = bergungsAnmeldungen; }
    
    public List<Verschollen> getVerschollenMeldungen() { return verschollenMeldungen; }
    public void setVerschollenMeldungen(List<Verschollen> verschollenMeldungen) { this.verschollenMeldungen = verschollenMeldungen; }
    
    
    
    
    
    
    
    

	public int getNr() {
		return nr;
	}
	
	

	public String getStandort() {
		return standort;
	}
	
	public void setStandort(String standort) {
		this.standort = standort;
	}
	
	
	
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	
	
	
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}

	
	
	
	
	
	public String getGroeße() {
		return groeße;
	}

	public void setGroeße(String groeße) {
		this.groeße = groeße;
	}
	
	
	
	

	
	public NetzStatus getNetzStatus() {
	    return netzStatus;
	}
	  
	public void setNetzStatus(NetzStatus netzStatus) {
	    this.netzStatus = netzStatus;
	}
	
	
	
	
	

	public String getBild() {
		return bild;
	}

	public void setBild(String bild) {
		this.bild = bild;
	}


}