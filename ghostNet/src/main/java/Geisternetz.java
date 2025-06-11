import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Geisternetz implements Serializable {
	private static final long serialVersionUID =1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nr;

    private String standort;
    private double lat; // Breitengrad
    private double lng; // Längengrad
    private String groeße;
    private String bild;
    @Enumerated(EnumType.STRING)
    private NetzStatus netzStatus;
    
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