package hotelbooking.Model;


public class Room implements Comparable<Room>{

    private double roomPrice;
    private int roomNumber;
        //^ Utvide med spesielle romnavn
            private String specialName;
            private boolean specialNameBoolean;
    private Hotel hotel;

//^ Kan utvides til spesielle romnavn som Kongesuite, Topsuite, osv.
    public Room(Hotel hotel, String roomNumberOrName, double roomPrice) {
        this.roomPrice = roomPrice;
        this.hotel = hotel;   
        try {
            this.roomNumber = Integer.parseInt(roomNumberOrName);
        }
        catch (Exception e) {
            this.specialName = roomNumberOrName;
            this.specialNameBoolean = true;
        } 
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getRoomPrice() {
        return roomPrice;
    }
                                //^ Utvide med spesielle romnavn 
                                public boolean hasSpecialName() {
                                    return specialNameBoolean;
                                }

                                public String getSpecialName() {
                                    return specialName;
                                }
    @Override
    public String toString() {
        if (this.specialNameBoolean) {
            return "Rom: " + specialName + " (" + roomPrice + " kr/natt)";
        }
        return "Rom nr: " + roomNumber + " (" + roomPrice + " kr/natt)";        
    }

    @Override
    public int compareTo(Room otherRoom) {
        return (int) (this.getRoomPrice() - otherRoom.getRoomPrice());
    }

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Room room = new Room(hotel, "Hei", 200);

    }
}
