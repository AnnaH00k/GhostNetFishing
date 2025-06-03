import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class GeisternetzListe
{
    private List<Geisternetz> fundstücke = new ArrayList<Geisternetz>();
    private Geisternetz neuesGeisternetz = new Geisternetz(0, "", 0.00, 0.00, "",NetzStatus.GEMELDET, ""); 
    private int aktuellerIndex = 0;


   
    public GeisternetzListe() {
        fundstücke.add(new Geisternetz(1, "Pazifik nahe Hawaii", 20.7984, -156.3319,
                "300 Quadratmeter", NetzStatus.GEMELDET, "resources/images/ghost-net-underwater.jpg"));

        fundstücke.add(new Geisternetz(2, "Nordatlantik (Bermuda-Dreieck)", 25.0000, -71.0000,
                "Pinnadelkopf-Größe", NetzStatus.GEMELDET, "resources/images/ghost-net-underwater.jpg"));

        fundstücke.add(new Geisternetz(3, "Mittelmeer südlich von Sizilien", 36.0000, 14.0000,
                "1 Kartoffelsack", NetzStatus.GEMELDET, "resources/images/ghost-net-underwater.jpg"));

        fundstücke.add(new Geisternetz(4, "Indischer Ozean westlich von Indonesien", -5.0000, 105.0000,
                "Netzreste", NetzStatus.GEMELDET, "resources/images/ghost-net-underwater.jpg"));

        fundstücke.add(new Geisternetz(5, "Südchinesisches Meer", 15.0000, 115.0000,
                "Großes Schleppnetz", NetzStatus.GEMELDET, "resources/images/ghost-net-underwater.jpg"));
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
	        fundstücke.add(new Geisternetz(neueID, neuesGeisternetz.getStandort(),neuesGeisternetz.getLat(),neuesGeisternetz.getLng(), neuesGeisternetz.getGroeße(),neuesGeisternetz.getNetzStatus(), neuesGeisternetz.getBild()));
	        neuesGeisternetz = new Geisternetz(0, "",0.00, 0.00, "", NetzStatus.GEMELDET, "");
	    }
	 
	 
	 public Geisternetz getAktuellesGeisternetz() {
	        if (!fundstücke.isEmpty() && aktuellerIndex >= 0 && aktuellerIndex < fundstücke.size()) {
	            return fundstücke.get(aktuellerIndex);
	        }
	        return null;
	    }

	 public List<NetzStatus> getNetzStatus() {
		    return Arrays.asList(NetzStatus.values());
		}
	 
	    public void naechstesGeisternetz() {
	        if (aktuellerIndex < fundstücke.size() - 1) {
	            aktuellerIndex++;
	        }
	    }

	    public void vorherigesGeisternetz() {
	        if (aktuellerIndex > 0) {
	            aktuellerIndex--;
	        }
	    }

	    public boolean isErstesGeisternetz() {
	        return aktuellerIndex == 0;
	    }

	    public boolean isLetztesGeisternetz() {
	        return aktuellerIndex >= fundstücke.size() - 1;
	    }
}