public class Geisternetz
{
    private int nr;

    private String standort;

    private String groeße;

    private String status;
    
    private String bild;

    public Geisternetz(int nr, String standort, String groeße, String status, String bild)
    {
        this.setNr(nr);
        this.setStandort(standort);
        this.setGroeße(groeße);
        this.setStatus(status);
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

	public void setStandort(String standort) {
		this.standort = standort;
	}

	public String getGroeße() {
		return groeße;
	}

	public void setGroeße(String groeße) {
		this.groeße = groeße;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBild() {
		return bild;
	}

	public void setBild(String bild) {
		this.bild = bild;
	}


}