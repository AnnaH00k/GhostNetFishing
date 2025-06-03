
public class Person
{
	 
    private int nr;

    private String name;

    private String telefonnummer;

    private String bild;
    
    private RollenTyp rollenTyp;
    private String passwort; 


    
    public Person(int nr, String name, String telefonnummer, String bild, RollenTyp rollenTyp, String passwort)
    {
        this.nr = nr;
        this.setName(name);
        this.setTelefonnummer(telefonnummer);
        this.setBild(bild);
        this.setRollenTyp(rollenTyp);
        this.setPasswort(passwort);

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
    
    public RollenTyp getRollenTyp() {
        return rollenTyp;
    }
    public String getPasswort()
    {
        return passwort;
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

	 public void setRollenTyp(RollenTyp rollenTyp) {
	        this.rollenTyp = rollenTyp;
	    }
	 public void setPasswort(String passwort)
	   {
	       this.passwort = passwort;
	   }

}