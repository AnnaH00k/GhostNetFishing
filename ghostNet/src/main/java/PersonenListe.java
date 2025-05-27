import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class PersonenListe
{
    private List<Person> nutzer = new ArrayList<Person>();
    private Person neuePerson = new Person(0, "", "", "");
    private int aktuelleIndex = 0;

   
    public PersonenListe()
    {
    	nutzer.add(new Person(1, "Liane Herbert",
                "0874168602", "https://cdn.pixabay.com/photo/2017/08/04/11/49/person-2579938_1280.jpg"));
    	nutzer.add(new Person(2, "Hugo Müller",
                "7250412052",
                "https://cdn.pixabay.com/photo/2018/10/29/21/46/human-3782189_1280.jpg"));
    	nutzer.add(new Person(3, "MauMau Moe",
                "0000000000", "resources/images/cat.jpeg"));
    }

    public List<Person> getNutzer()
    {
        return nutzer;
    }

	public Person getNeuePerson() {
		return neuePerson;
	}

	public void setNeuePerson(Person neuePerson) {
		this.neuePerson = neuePerson;
	}
	
	 public void personHinzufuegen() {
	        int neueID = nutzer.size() + 1;
	        neuePerson.setNr(neueID);
	        nutzer.add(new Person(neueID, neuePerson.getName(), neuePerson.getTelefonnummer(), neuePerson.getBild()));
	        neuePerson = new Person(0, "", "", "");
	    }

	 
	 
	 
	 
	 
	 
	 public Person getAktuellerNutzer() {
	        if (!nutzer.isEmpty() && aktuelleIndex >= 0 && aktuelleIndex < nutzer.size()) {
	            return nutzer.get(aktuelleIndex);
	        }
	        return null;
	    }

	    public void naechsterNutzer() {
	        if (aktuelleIndex < nutzer.size() - 1) {
	            aktuelleIndex++;
	        }
	    }

	    public void vorherigerNutzer() {
	        if (aktuelleIndex > 0) {
	            aktuelleIndex--;
	        }
	    }

	    public int getAktuelleIndex() {
	        return aktuelleIndex;
	    }

	    public void setAktuelleIndex(int aktuelleIndex) {
	        this.aktuelleIndex = aktuelleIndex;
	    }

	    public boolean isErsterNutzer() {
	        return aktuelleIndex == 0;
	    }

	    public boolean isLetzterNutzer() {
	        return aktuelleIndex >= nutzer.size() - 1;
	    }
	}