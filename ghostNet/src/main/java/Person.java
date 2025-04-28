public class Person
{
    private int nr;

    private String name;

    private String telefonnummer;

    private String bild;
    
    private boolean istMeldendePerson;
    
    private boolean istBergendePerson;

    public Person(int nr, String name, String telefonnummer, String bild)
    {
        this.nr = nr;
        this.setName(name);
        this.setTelefonnummer(telefonnummer);
        this.setBild(bild);
    }

    
    public int getNr()
    {
        return nr;
    }
    public String getName()
    {
        return name;
    }

    public String getTelefonnummer()
    {
        return telefonnummer;
    }

    public String getBild()
    {
        return bild;
    }
    
    public void setNr(int nr) {
    	this.nr = nr;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public void setBild(String bild) {
		this.bild = bild;
	}


	public boolean isIstMeldendePerson() {
		return istMeldendePerson;
	}


	public void setIstMeldendePerson(boolean istMeldendePerson) {
		this.istMeldendePerson = istMeldendePerson;
	}


	public boolean isIstBergendePerson() {
		return istBergendePerson;
	}


	public void setIstBergendePerson(boolean istBergendePerson) {
		this.istBergendePerson = istBergendePerson;
	}
}