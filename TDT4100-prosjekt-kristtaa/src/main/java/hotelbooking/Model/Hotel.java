package hotelbooking.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Hotel {
    
    // private Room chosenRoom;
    private HashMap<Room, List<List<String>>> allRoomsWithBookings;
    private List<Room> availableRooms;
    
    private Room room500;
    private Room room100;
    private Room room300;
    private Room roomPlatinumSuite;
    private Room roomGoldSuite;


    public Hotel() {
        
        this.allRoomsWithBookings = new HashMap<>();
        this.availableRooms = new ArrayList<>();
    
        this.room500 = new Room(this, "500", 100);
        this.room100 = new Room(this, "100", 200);
        this.room300 = new Room(this, "300", 300);
        this.roomPlatinumSuite = new Room(this, "Platinum Suite", 1300);
        this.roomGoldSuite = new Room(this, "Gold Suite", 1000);
                
        this.allRoomsWithBookings.put(room500, new ArrayList<>(new ArrayList<>()));
        this.allRoomsWithBookings.put(room100, new ArrayList<>(new ArrayList<>()));
        this.allRoomsWithBookings.put(room300, new ArrayList<>(new ArrayList<>()));
        this.allRoomsWithBookings.put(roomPlatinumSuite, new ArrayList<>(new ArrayList<>()));
        this.allRoomsWithBookings.put(roomGoldSuite, new ArrayList<>(new ArrayList<>()));
        
        // this.updateAvailableRoomsForTimeframe(LocalDate.MIN, LocalDate.MIN);
    }

//! Gir ut ledige rom
    public void updateAvailableRoomsForTimeframe(LocalDate wantedFromDate, LocalDate wantedToDate) {
        if (!this.areValidDates(wantedFromDate, wantedToDate)){
            throw new IllegalArgumentException("Ugyldige datoer generelt");
        }
        this.availableRooms.clear();
        //? Sjekker gjennom alle nøkler (rom på hotellet)
        
        
        for (Room keyRoom : this.allRoomsWithBookings.keySet()) {
            //? Hvis rommet ikke har noen bookings så er det ledig
            if (this.allRoomsWithBookings.get(keyRoom).isEmpty()){
                this.availableRooms.add(keyRoom);
                continue;
            } 
            //? Sjekker om for hver booking lagt inn, om datoene tilsier at det ikke er ledig i kundes ønskede vindu
            for (List<String> booking : this.allRoomsWithBookings.get(keyRoom)) {
                //? Fast format på booking-entries er:
                /*
                 * [FNr, (fromDate), String(toDate)]
                 */
                List<List<String>> roomBookings = this.allRoomsWithBookings.get(keyRoom);

                LocalDate bookedFromDate = LocalDate.parse(booking.get(1));
                LocalDate bookedToDate = LocalDate.parse(booking.get(2));

                //? Finner sluttcaset hvor vi er på siste booking i lista og da må anse rommet som ledig 
                //? hvis også den er gyldig
                if (this.areValidBookingDates(wantedFromDate, wantedToDate, bookedFromDate, bookedToDate) && roomBookings.indexOf(booking) == roomBookings.size()-1){
                    this.availableRooms.add(keyRoom);
                    break;
                }

                if (!this.areValidBookingDates(wantedFromDate, wantedToDate, bookedFromDate, bookedToDate)){
                    break;
                }
            }
        }
        // System.out.println("Alle rom og deres bookinger: " + allRoomsWithBookings);
        // System.out.println("_______________________________");
        // System.out.println("Ledige rom for perioden (" + wantedFromDate + " til " + wantedToDate +"): " + this.availableRooms);
        // System.out.println("_______________________________");
    }

    public boolean areValidBookingDates(LocalDate wantedFromDate, LocalDate wantedToDate, LocalDate bookedFromDate, LocalDate bookedToDate) {
                        // LocalDate now = LocalDate.now();
                        // if (wantedFromDate.isBefore(now) || wantedToDate.isBefore(now)) return false; //Kan ikke booke i fortiden"
                        // if (!wantedFromDate.isBefore(wantedToDate)) return false; //"Ugyldige datoer"); // Kan ikke sjekke ut før du sjekker inn
                        // // if (!this.areValidDates(fromDate, toDate)) throw new IllegalArgumentException("Ugyldige datoer");

        if (!this.areValidDates(wantedFromDate, wantedToDate)) return false;


        if (wantedFromDate.isAfter(bookedFromDate) && wantedFromDate.isBefore(bookedToDate)) return false;
        if (wantedToDate.isAfter(bookedFromDate) && wantedToDate.isBefore(bookedToDate)) return false;
        
        if (wantedFromDate.isBefore(bookedToDate)) {
            if (!wantedToDate.isBefore(bookedFromDate)){
                return false;
            } 
        }   
        if (wantedFromDate.isAfter(bookedToDate)){
            if (wantedToDate.isBefore(bookedFromDate)) return false; //"Rommet er ikke ledig i denne perioden 2");
        }
        return true;
    }

    private boolean areValidDates(LocalDate wantedFromDate, LocalDate wantedToDate) {
        LocalDate now = LocalDate.now();
        if (wantedFromDate.isBefore(now) || wantedToDate.isBefore(now) || wantedFromDate.isEqual(now) || wantedFromDate.isEqual(now)  ) return false; //Kan ikke booke i fortiden"
        if (!wantedFromDate.isBefore(wantedToDate)) return false;
        return true;
    }
    

    public void bookRoomInHotel(Room room, ValidatedPerson person, LocalDate wantedFromDate, LocalDate wantedToDate) {
        this.updateAvailableRoomsForTimeframe(wantedFromDate, wantedToDate);
        if (!this.availableRooms.contains(room)) throw new IllegalArgumentException("Ikke ledig rom");
        this.allRoomsWithBookings.get(room).add(List.of(person.toString(), "" + wantedFromDate, "" + wantedToDate));
        
        System.err.println("~~~~~~~~~~~~~~~~\n" + "Rombooking av romnr: " + room.getRoomNumber() + 
            "\n________________\n" + "Room booket fra " + 
            wantedFromDate + " til " + wantedToDate + " for " + person +".\n" + "Det blir: " + 
            calculatePrice(room.getRoomPrice(), this.getNumOfDaysForStay(wantedFromDate, wantedToDate))
            + "kr." + "\n~~~~~~~~~~~~~~~~");
    }



    public void readLinesFromFile(List<String> lines) {
        this.allRoomsWithBookings.clear();
        
        this.allRoomsWithBookings.put(room500, new ArrayList<>());
        this.allRoomsWithBookings.put(room100, new ArrayList<>());
        this.allRoomsWithBookings.put(room300, new ArrayList<>());
        this.allRoomsWithBookings.put(roomPlatinumSuite, new ArrayList<>());
        this.allRoomsWithBookings.put(roomGoldSuite, new ArrayList<>());
        
        for (String line : lines) {
            String[] elements = line.split(",");

            String roomNumberOrName = elements[0];
            String personFNr = elements[1];
            String bookedFromDateString = elements[2];
            String bookedToDateString = elements[3];
            int roomNumber = 0;
            try {
                roomNumber = Integer.parseInt(roomNumberOrName);
            }
            catch (Exception e) {
                System.out.println("Romnummer var ikke tall");
            }

            
            if (roomNumber == room500.getRoomNumber()) {
                this.allRoomsWithBookings.get(room500).add(List.of(personFNr, bookedFromDateString, bookedToDateString));    
            }
            else if (roomNumber == room300.getRoomNumber()) {
                this.allRoomsWithBookings.get(room300).add(List.of(personFNr, bookedFromDateString, bookedToDateString));    
            }
            else if (roomNumber == room100.getRoomNumber()) {
                this.allRoomsWithBookings.get(room100).add(List.of(personFNr, bookedFromDateString, bookedToDateString));    
            }
            else if (roomNumberOrName.equals(roomPlatinumSuite.getSpecialName())) {
                this.allRoomsWithBookings.get(roomPlatinumSuite).add(List.of(personFNr, bookedFromDateString, bookedToDateString));    
            }
            else if (roomNumberOrName.equals(roomGoldSuite.getSpecialName())) {
                this.allRoomsWithBookings.get(roomGoldSuite).add(List.of(personFNr, bookedFromDateString, bookedToDateString));    
            }
        }
        System.out.println(this.allRoomsWithBookings);
    }

    public List<Room> getAvailableRooms() {
        return new ArrayList<>(this.availableRooms);
    }

    public HashMap<Room, List<List<String>>> getAllRoomsWithBookings() {
        return new HashMap<>(this.allRoomsWithBookings);
    }

    public int getNumOfDaysForStay(LocalDate wantedFromDate, LocalDate wantedToDate) {
        return (int) wantedFromDate.until(wantedToDate, ChronoUnit.DAYS);
    }
    
//! Beregne pris for booking av rom
    public static double calculatePrice(double price, int days) {
        return price*days;
    }

//! Sorteringsknapper for pris og romnummer i oversikten over ledige rom
    public void sortByPrice(){
        Collections.sort(this.availableRooms);
    }

    public void sortByRoomNumber(){
        Collections.sort(this.availableRooms, new RoomNumberComparator());
    }

    

    @Override
    public String toString() {
        return "Hotel med rommene :\n" + allRoomsWithBookings;
    }

    public static void main(String[] args) {
        // Hotel hotel = new Hotel();
        // System.out.println(hotel);
        // hotel.availableRoomsForTimeframe(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        // System.out.println(hotel.availableRooms);
        // hotel.bookRoom(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
        // System.out.println(hotel);
        // hotel.sortByRoomNumber();
        // System.out.println(hotel);

        
        Hotel hotel = new Hotel();
        hotel.updateAvailableRoomsForTimeframe(LocalDate.parse("2023-04-02"), LocalDate.parse("2023-04-05"));
        System.out.println(hotel.getAvailableRooms());

       


                            // hotel.sortByPrice();
                            //     // hotel.updateAvailableRoomsForTimeframe(LocalDate.parse("2023-04-02"), LocalDate.parse("2023-04-05"));
                            //     System.out.println("Sortert pris: " + hotel.getAvailableRooms());
                            // hotel.sortByRoomNumber();
                            //     // hotel.updateAvailableRoomsForTimeframe(LocalDate.parse("2023-04-02"), LocalDate.parse("2023-04-05"));
                            //     System.out.println("Sortert nummer: " + hotel.getAvailableRooms());
                            // hotel.sortByPrice();
                            //     // hotel.updateAvailableRoomsForTimeframe(LocalDate.parse("2023-04-02"), LocalDate.parse("2023-04-05"));
                            //     System.out.println("Sortert pris 2: " + hotel.getAvailableRooms());

        ValidatedPerson per = new ValidatedPerson("Per","Hansen", 50, "M", "per.hansen@gmail.com", "16117312345");

        hotel.bookRoomInHotel(hotel.room500, per, LocalDate.parse("2023-04-04"), LocalDate.parse("2023-04-07"));
        System.err.println(hotel);
        hotel.bookRoomInHotel(hotel.room500, per, LocalDate.parse("2023-04-08"), LocalDate.parse("2023-04-09"));
        System.err.println(hotel);
        // hotel.bookRoomInHotel(hotel.room500, "16110212345", LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-09"));
        // System.err.println(hotel);
        hotel.bookRoomInHotel(hotel.room100, per, LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-09"));
        System.err.println(hotel);
        hotel.bookRoomInHotel(hotel.room100, per, LocalDate.parse("2023-04-02"), LocalDate.parse("2023-04-03"));
        System.err.println(hotel);

        
        System.out.println("Writing state to file:\n");
        try {
            FileHelper.writeStateToFile("c:/Users/kraa1/Documents/OvingerOgSlikt/tdt4100-students-23/TDT4100-prosjekt-kristtaa/src/main/resources/hotelbooking/hotelStateFile.txt", 
                hotel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        hotel.allRoomsWithBookings.clear();
        System.out.println(hotel.allRoomsWithBookings);
        try {
            FileHelper.readStateFromFile("c:/Users/kraa1/Documents/OvingerOgSlikt/tdt4100-students-23/TDT4100-prosjekt-kristtaa/src/main/resources/hotelbooking/hotelStateFile.txt", 
                hotel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Writing state to file:\n");
        try {
            FileHelper.writeStateToFile("c:/Users/kraa1/Documents/OvingerOgSlikt/tdt4100-students-23/TDT4100-prosjekt-kristtaa/src/main/resources/hotelbooking/hotelStateFile.txt", 
                hotel);
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
