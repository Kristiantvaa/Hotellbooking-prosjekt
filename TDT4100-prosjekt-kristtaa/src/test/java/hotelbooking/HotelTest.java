package hotelbooking;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hotelbooking.Model.FileHelper;
import hotelbooking.Model.Hotel;
import hotelbooking.Model.Room;
import hotelbooking.Model.RoomNumberComparator;
import hotelbooking.Model.ValidatedPerson;

public class HotelTest {
    
    private Hotel hotel;
    private ValidatedPerson person;

    private String validFirstName = "Kristian";
    private String validLastName = "Aastveit";
    private int validAge = 20;
    private String validGender = "M";
    private String validEmail = "kristian.aastveit@gmail.com";
    private String validFNr =  "16110212345";

//TODO Initialisering før alle tester
    @BeforeEach
    public void beforeEachTest() {
        this.hotel = new Hotel();
            
        person = new ValidatedPerson("Kristian", "Aastveit", 20, "M", "kristian.aastveit@gmail.com", "16110212345");
        this.hotel.bookRoomInHotel(this.getRoomForBooking(2), person, LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
        System.out.println(hotel);
        System.err.println("SETUP FERDIG");
    }

//TODO Hjelpemetoder for testene
private boolean roomIsBooked (Room room, LocalDate wantedFromDate, LocalDate wantedToDate) {
    this.hotel.updateAvailableRoomsForTimeframe(wantedFromDate, wantedToDate);
    if (this.hotel.getAvailableRooms().contains(room)) return false;
    return true;
}

private Room getRoomForBooking(int index) {
    List<Room> rooms = new ArrayList<>(hotel.getAllRoomsWithBookings().keySet());
    Collections.sort(rooms, new RoomNumberComparator());
    // Indeks 0 er room100
    // Indeks 1 er room300
    // Indeks 2 er room500
    Room room = rooms.get(index);
    System.err.println(room.getRoomNumber()+".......");
    return room;
}


//TODO Testene:
//! Testing av skriving til og lesing av fil
    @Test
    @DisplayName("Teste riktig lagring til fil")
    public void testWritingToStateFile() {
        LocalDate wantedFromDate = LocalDate.now().plusDays(2);
        LocalDate wantedToDate = LocalDate.now().plusDays(4);
        Room roomBooking = this.getRoomForBooking(0);
        
        this.hotel.bookRoomInHotel(roomBooking, person, wantedFromDate, wantedToDate);
        HashMap<Room, List<List<String>>> expectedAllRoomsWithBookings = this.hotel.getAllRoomsWithBookings();

        try {
            FileHelper.writeStateToFile("c:/Users/kraa1/Documents/OvingerOgSlikt/tdt4100-students-23/TDT4100-prosjekt-kristtaa/src/test/java/hotelbooking/testHotelStateFile.txt", 
                hotel);
        } catch (IOException e) {
            System.err.println("Gikk ikke an å skrive til fil");
            e.printStackTrace();
        }

        try {
            FileHelper.readStateFromFile("c:/Users/kraa1/Documents/OvingerOgSlikt/tdt4100-students-23/TDT4100-prosjekt-kristtaa/src/test/java/hotelbooking/testHotelStateFile.txt", 
                hotel);
        } catch (IOException e) {
            System.err.println("Greide ikke å lese fra fil");
            e.printStackTrace();
        }

        assertEquals(expectedAllRoomsWithBookings, this.hotel.getAllRoomsWithBookings());
        System.err.println("Leste riktig fra fil");
    }


//! Testing av datoer
    @Test
    @DisplayName("Teste booking før nåtid datoer")
    public void testDateBookingBeforeNow() {
        LocalDate now = LocalDate.now();
        LocalDate wantedFromDate = LocalDate.now().minusDays(3);
        LocalDate wantedToDate = LocalDate.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> this.hotel.bookRoomInHotel(this.getRoomForBooking(1), person, wantedFromDate, wantedToDate), "Kan ikke booke før nåtid");
        assertThrows(IllegalArgumentException.class, () -> this.hotel.bookRoomInHotel(this.getRoomForBooking(1), person, now, wantedToDate), "Kan ikke booke fra idag");
        assertThrows(IllegalArgumentException.class, () -> this.hotel.bookRoomInHotel(this.getRoomForBooking(1), person, wantedFromDate, now), "Kan ikke booke til idag");
        assertThrows(IllegalArgumentException.class, () -> this.hotel.bookRoomInHotel(this.getRoomForBooking(1), person, now, now), "Kan ikke booke fra idag til idag");
    }


    @Test
    @DisplayName("Teste booking med feil rekkefølge på datoer")
    public void testCheckOutBeforeCheckIn() {
        LocalDate wantedFromDate = LocalDate.now().plusDays(2);
        LocalDate wantedToDate = LocalDate.now().plusDays(4);
        assertThrows(IllegalArgumentException.class, () -> this.hotel.bookRoomInHotel(this.getRoomForBooking(1), person, wantedToDate, wantedFromDate), "Kan ikke booke fra senere dato til en tidligere en");
        System.out.println(hotel + "\nFikk sjekket ut før inn");
    }


    @Test
    @DisplayName("Prøver å booke et opptatt rom")
    public void testBookingAlreadyBookedRoom() {
        LocalDate wantedFromDate = LocalDate.now().plusDays(2);
        LocalDate wantedToDate = LocalDate.now().plusDays(4);
        assertThrows(IllegalArgumentException.class, () -> this.hotel.bookRoomInHotel(this.getRoomForBooking(2), person, wantedToDate, wantedFromDate), "Kan ikke booke fra senere dato til en tidligere en");
        System.out.println(hotel + "\nKan ikke booke rommet som er opptatt");
    }

   
//! Teste at booking blir gjennomført med riktige datoer og bookinginfo
    @Test
    @DisplayName("Teste at booking blir gjennomført med riktige datoer og bookinginfo")
    public void testCorrectBooking() {
        LocalDate wantedFromDate = LocalDate.now().plusDays(2);
        LocalDate wantedToDate = LocalDate.now().plusDays(4);
        Room roomBooking = this.getRoomForBooking(0);
        
        this.hotel.bookRoomInHotel(roomBooking, person, wantedFromDate, wantedToDate);
        assertTrue(this.roomIsBooked(roomBooking, wantedFromDate, wantedToDate), "Rommet skal være booket nå");
        System.out.println(hotel + "\nRom booket: " + roomBooking.getRoomNumber());

    }
    
    

//! Testing av bookinginfo
    @Test
    @DisplayName("Har riktig navn")
    public void testCustomerNames() {
        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson("kristian", "aastveit", validAge, validGender, validEmail, validFNr),
            "Må ha stor forbokstav i fornavn og etternavn");
        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson("Kristian", "aastveit",  validAge, validGender, validEmail, validFNr),
            "Må ha stor forbokstav i etternavn");
        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson("kristian", "Aastveit",  validAge, validGender, validEmail, validFNr),
            "Må ha stor forbokstav i fornavn");

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson("k p", "Aastveit",  validAge, validGender, validEmail, validFNr),
            "Kan ikke ha mellomrom i navn");
        
        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson("k1", "Aastveit",  validAge, validGender, validEmail, validFNr),
            "Kan kun ha bokstaver i navn");
    }  


    @Test
    @DisplayName("Kunde er gammel nok")
    public void testCustomerAge() {
        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, 17, validGender, validEmail, validFNr),
        "For ung kunde");
        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, 111, validGender, validEmail, validFNr),
        "Urealistisk å være så gammel");
    }


    @Test
    @DisplayName("Kunde M og F er gyldige kjønn")
    public void testCustomerGender() {
        String[] letters = {"A","B","C","D","E","G","H","I","J","K","L","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","Æ","Ø","Å"};
        for (String testLetter : letters) {
            assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, testLetter, validEmail, validFNr),
        "Ugyldig kjønn");
        }        
    }


    @Test
    @DisplayName("Har riktig mail")
    public void testCustomerEmail() {

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, "kristian.aastveitgmail.com", validFNr),
        "Må ha @");

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, "kristian.aastveit@bing.com", validFNr),
        "Må bruke gyldig server");

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, "kristian.aastveit@gmail.tv", validFNr),
        "Må bruke gyldig domene");

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, "kristianaastveit@gmail.com", validFNr),
        "Må ha fornavn.etternavn");

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, "kristian.aastve@gmail.com", validFNr),
        "Må ha skrive hele etternavnet i mailen din");

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, "kristi.aastveit@gmail.com", validFNr),
        "Må ha skrive hele fornavnet i mailen din");
    }


    @Test
    @DisplayName("Kundes fødselsnummer er gyldig")
    public void testCustomerFNr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append("1");
            assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, validEmail, sb.toString()),
            "Må være 11 tegn");
        }

        assertThrows(IllegalArgumentException.class, () -> new ValidatedPerson(validFirstName, validLastName, validAge, validGender, validEmail, "1611021234p"),
            "Kan kun inneholde siffere");

    }
}
