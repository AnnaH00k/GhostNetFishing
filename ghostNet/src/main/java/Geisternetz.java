public class Geisternetz
{
    private int nr;

    private String standort;
    
    private double lat; // Breitengrad
    private double lng; // Längengrad


    private String groeße;

    private String status;
    
    private String bild;
    
    private NetzStatus netzStatus;

    public Geisternetz(int nr, String standort, double lat, double lng, String groeße, NetzStatus netzStatus, String bild)
    {
        this.setNr(nr);
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

	public void setNr(int nr) {
		this.nr = nr;
	}

	public String getStandort() {
		return standort;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}

	public void setStandort(String standort) {
		this.standort = standort;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
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