package hotelbooking.Model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class FileHelper {
    

    public static void writeStateToFile (String filePath, Hotel hotel) throws IOException {
        // Filer skal lagres på formatet:
        // person(fødselsnummer), romnummer, dato fra, dato til
        HashMap<Room, List<List<String>>> allRoomsWithBookings = hotel.getAllRoomsWithBookings();
        // Path sti = Path.of("hotelStateFile.txt");
        // System.out.println(sti);
        
        File hotelStateFile = new File(filePath);
        
        // System.out.println(hotelStateFile.getAbsolutePath());
        // System.out.println(hotelStateFile.toPath());
       

        if (hotelStateFile.createNewFile()) {
            System.out.println("Lagde ny fil " + hotelStateFile);
        }

        PrintWriter writer = new PrintWriter(hotelStateFile);

        for (Room keyRoom : allRoomsWithBookings.keySet()) {
            for (List<String> booking : allRoomsWithBookings.get(keyRoom)) {
                String formattedBookingString = "" + keyRoom.getRoomNumber() + "," + booking.get(0) + "," 
                + booking.get(1) + "," + booking.get(2);
                if (keyRoom.hasSpecialName()) {
                    formattedBookingString = "" + keyRoom.getSpecialName() + "," + booking.get(0) + "," 
                    + booking.get(1) + "," + booking.get(2);
                }
                // System.out.println(formattedBookingString);
                writer.println(formattedBookingString);
            }
        }
        writer.flush();
        writer.close();
    }

    public static void readStateFromFile (String filePath, Hotel hotel) throws IOException {
        File hotelStateFile = new File(filePath);
        Scanner reader = new Scanner(hotelStateFile);
        List<String> lines = new ArrayList<>();
        while (reader.hasNextLine()) {
            lines.add(reader.nextLine());
        }
        reader.close();
        hotel.readLinesFromFile(lines);
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("34"));
        System.out.println(Integer.parseInt("hei"));

    }
}
