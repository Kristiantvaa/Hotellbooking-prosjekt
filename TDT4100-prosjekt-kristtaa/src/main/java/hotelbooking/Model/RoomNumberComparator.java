package hotelbooking.Model;

import java.util.Comparator;

public class RoomNumberComparator implements Comparator<Room>{

    @Override
    public int compare(Room room0, Room room1) {
        if (room0.hasSpecialName() && !room1.hasSpecialName()){
            return -1;
        }
        
        else if (!room0.hasSpecialName() && room1.hasSpecialName()){
            return 1;
        }
        
        else if (room0.hasSpecialName() && room1.hasSpecialName()){
            return -room0.getSpecialName().compareTo(room1.getSpecialName());
            //! Minus for å reversere sånn at størst navn er først
        }

        return room1.getRoomNumber() - room0.getRoomNumber();
        //! Byttet om på 0 og 1 for at vi skal få størst navn og tall øverst
    }
    
}
