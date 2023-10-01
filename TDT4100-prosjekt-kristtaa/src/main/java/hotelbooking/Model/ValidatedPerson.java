package hotelbooking.Model;

import java.util.ArrayList;
import java.util.List;

public class ValidatedPerson {

    
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String email;
        private List<String> validServers = new ArrayList<>(List.of("gmail", "yahoo", "hotmail", "live", "ntnu"));
        private List<String> validDomains = new ArrayList<>(List.of("com", "co", "no"));
    private String FNr;


    public ValidatedPerson(String firstName, String lastName, int age, String gender, String email, String fNr) {
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setGender(gender);
        setEmail(email);
        setFNr(fNr);
    }

//! Validering av kundes informasjon
    private boolean isValidName(String name) {
        //Krav for et navn:
        /*
         * Kun bokstaver
         * Kun første bokstav kan være stor, og den MÅ være stor
         * Resten av bokstavene må være små
         */
        char[] nameSymbols = name.toCharArray();
        for (char c : nameSymbols) {
            if (!(Character.isAlphabetic(c))) {
                System.out.println("her");
                throw new IllegalArgumentException("Ugyldig navn");
            }
            if (name.indexOf(String.valueOf(c)) != 0 && Character.isUpperCase(c)) {
                System.out.println(name.indexOf(String.valueOf(c)));
                throw new IllegalArgumentException("Ugyldig navn");
            }
            if (name.indexOf(String.valueOf(c)) == 0 && Character.isLowerCase(c)) {
                System.out.println(name.indexOf(String.valueOf(c)));
                throw new IllegalArgumentException("Ugyldig navn");
            }
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        //Krav for email:
        /*
         * firstName.lastName@server.domene
         * navn: alle navnene som er i personnavn
         *  domene: kun .no, .co, .com
         *  server: gmail, yahoo, hotmail, live, ntnu
         */

            if (email.indexOf("@") < 0) throw new IllegalArgumentException("Må ha @ i en mail");
        String namesPart = email.substring(0, email.indexOf("@"));
          //Hvis ingen punktum mellom fornavn og etternavn
          if (email.indexOf(".") > email.indexOf("@")) throw new IllegalArgumentException("Ugyldig fornavn.etternavn");

            String firstName = namesPart.substring(0, namesPart.indexOf("."));
            String lastName = namesPart.substring(namesPart.indexOf(".")+1, namesPart.length());
        String mailAndDomainpart = email.substring(email.indexOf("@")+1, email.length());
            String mailPart = mailAndDomainpart.substring(0, mailAndDomainpart.indexOf("."));
            String domainPart = mailAndDomainpart.substring(mailAndDomainpart.indexOf(".")+1, mailAndDomainpart.length());

      
        if (!(firstName.toLowerCase().equals(this.firstName.toLowerCase()) && lastName.toLowerCase().equals(this.lastName.toLowerCase()) && this.validServers.contains(mailPart.toLowerCase()) && this.validDomains.contains(domainPart.toLowerCase()))) {
            throw new IllegalArgumentException("Ugyldig e-post");
        }
        return true;  
    }

    private boolean isValidFNr (String FNr) {
        if (FNr.length() != 11) throw new IllegalArgumentException("Ugyldig fødselsnummer");
        char[] FNrSymbols = FNr.toCharArray();
        for (char c : FNrSymbols) {
            if (!Character.isDigit(c)) throw new IllegalArgumentException("Kan kun inneholde siffere");
        }
        return true;
    }



//! Settere av kundens informasjon
    public void setFirstName(String firstName) {
        this.isValidName(firstName);
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.isValidName(lastName);
        this.lastName = lastName;
    }

    public void setAge(int age) {
        if (age < 18 || age > 110) throw new IllegalArgumentException("Ikke gammel nok til å booke rom");
        this.age = age;
    }

    public void setGender(String gender) {
        if (!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) throw new IllegalArgumentException("Ugyldig kjønn");
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.isValidEmail(email);
        this.email = email;
    }

    public void setFNr(String FNr) {
        this.isValidFNr(FNr);
        this.FNr = FNr;
    }

    @Override
    public String toString() {
        return this.FNr;
    }
}
