import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class GeisternetzListe
{
    private List<Geisternetz> fundstücke = new ArrayList<Geisternetz>();
    private Geisternetz neuesGeisternetz = new Geisternetz(0, "", "","", ""); 


   
    public GeisternetzListe()
    {
    	fundstücke.add(new Geisternetz(1, "Im Meer",
                "300 quadratmeter","verschollen", "resources/images/ghost-net-underwater.jpg"));
    	fundstücke.add(new Geisternetz(2, "Bermuda dreieck",
                "pinnadelkopf größe","geborgen",
                "resources/images/ghost-net-underwater.jpg"));
    	fundstücke.add(new Geisternetz(3, "Atlantis",
                "1 Kartoffelsack","bergung bevorstehend", "resources/images/ghost-net-underwater.jpg"));
    }

    public List<Geisternetz> getFundstücke()
    {
        return fundstücke;
    }

	public Geisternetz getNeuesGeisternetz() {
		return neuesGeisternetz;
	}

	public void setNeuesGeisternetz(Geisternetz neuesGeisternetz) {
		this.neuesGeisternetz = neuesGeisternetz;
	}
	 public void geisternetzHinzufuegen() {
	        int neueID = fundstücke.size() + 1;
	        neuesGeisternetz.setNr(neueID);
	        fundstücke.add(new Geisternetz(neueID, neuesGeisternetz.getStandort(), neuesGeisternetz.getGroeße(),neuesGeisternetz.getStatus(), neuesGeisternetz.getBild()));
	        neuesGeisternetz = new Geisternetz(0, "","", "", "");
	    }
}