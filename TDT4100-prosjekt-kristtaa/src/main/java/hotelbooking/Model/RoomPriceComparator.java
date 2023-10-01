package hotelbooking.Model;

import java.util.Comparator;


//TODO Tar dette som Comparable innebygd sortering for rom
public class RoomPriceComparator implements Comparator<Room>{

    @Override
    public int compare(Room room0, Room room1) {
        return (int) (room0.getRoomPrice() - room1.getRoomPrice());
    }
    
}
