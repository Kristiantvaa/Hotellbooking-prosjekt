package hotelbooking.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import hotelbooking.Model.FileHelper;
import hotelbooking.Model.Hotel;
import hotelbooking.Model.Room;
import hotelbooking.Model.ValidatedPerson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class HotelAppController {
    
//! Frontpage View
@FXML private Pane frontPageView;
    @FXML   private DatePicker checkInDate;
    @FXML   private DatePicker checkOutDate;
    
    @FXML   private ListView<Room> availableRoomsShowing;
    
    @FXML   private Button checkDate;
        @FXML   private Text illegalDate;
    @FXML   private Button continueToBooking;

//! Booking Page View
@FXML private Pane bookingPageView;
    @FXML   private TextField inputFirstName;
    @FXML   private TextField inputLastName;
    @FXML   private TextField inputAge;
    @FXML   private TextField inputGender;
    @FXML   private TextField inputEmail;
    @FXML   private TextField inputFNr;
    
    @FXML   private Button bookRoom;
        @FXML   private Text illegalPerson;

    @FXML   private Button goBackButton;

    @FXML   private Button sortByPrice;
    @FXML   private Button sortByRoomNumber;

    @FXML   private TextField chosenRoomNumberForStay;
    @FXML   private TextField numberOfDaysToStay;
    @FXML   private TextField totalPriceForStay;

//! Endscreen View
@FXML private Pane endScreen;
    @FXML   private Button goBackFromEndScreen; 

    private Hotel hotel;
    private boolean showingFrontPage = true;
    private boolean showBookButton;
    private List<TextField> allInputFields;
    


   

    @FXML
    public void initialize() {
        //^ Oppretter hotellet, skal vise fram ledige rom, knapper skal ikke fungere før noe er valgt.
        this.hotel = new Hotel();

        this.allInputFields = new ArrayList<>(List.of(inputFirstName, inputLastName, inputAge, 
    inputGender, inputEmail, inputFNr));  

        try {
            FileHelper.readStateFromFile("c:/Users/kraa1/Documents/OvingerOgSlikt/tdt4100-students-23/TDT4100-prosjekt-kristtaa/src/main/resources/hotelbooking/hotelStateFile.txt", hotel);
            System.out.println("Hentet fra fil");
            System.out.println(hotel.getAllRoomsWithBookings());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.setVisibilityAndAbilityForPage(this.frontPageView, true);
        this.setVisibilityAndAbilityForPage(this.bookingPageView, false);
        this.setVisibilityAndAbilityForPage(this.endScreen, false);
        
        this.illegalDate.setVisible(false);

        this.bookRoom.setDisable(true);
        this.checkDate.setDisable(true);
        this.continueToBooking.setDisable(true);
        this.sortByPrice.setDisable(true);
        this.sortByRoomNumber.setDisable(true);
    

    //! Gjør at Book-knappen ikke fungerer så lenge inputfeltene er tomme
        this.disableBookButtonWhileFieldsAreEmpty();
    //! Gjør at Gender-felt kun tar ett tegn
        this.setMaxLengthForTextFieldInput(this.inputGender, 1);
    //! Gjør at FNr-felt tar maks 11 tegn
        this.setMaxLengthForTextFieldInput(this.inputFNr, 11);
    //! Gjør at age-feltet kun tar maks 3 tegn
        this.setMaxLengthForTextFieldInput(this.inputAge, 3);
        

    }

private void setVisibilityAndAbilityForPage (Pane pane, boolean enable) {
    pane.setVisible(enable);
    pane.setDisable(!enable);
}

//! Gjør at Book-knappen ikke fungerer så lenge inputfeltene er tomme
    private void disableBookButtonWhileFieldsAreEmpty() {
        this.allInputFields.forEach(text -> text.textProperty().addListener((observable, oldValue, newValue) -> { 
            for (TextField textField : allInputFields) {
                if (textField.getText().isEmpty()) {
                    showBookButton = false;
                    break;
                }
                showBookButton = true;
            }
            this.bookRoom.setDisable(!showBookButton);
            })
        );
    }

//! Setter maks antall tegn for et felt
    private void setMaxLengthForTextFieldInput (TextField field, int maxLength) {
        field.textProperty().addListener((observable, oldValue, newValue) -> { 
            if (field.getText().length() > maxLength) {
                System.out.println(newValue);
                String maxLengthInput = field.getText().substring(0, maxLength);
                field.setText(maxLengthInput);
                }
            }
        );
    }

    @FXML
    public void onChosenDate() {
        if (this.checkInDate.getValue() != null && this.checkOutDate.getValue() != null) {
            this.checkDate.setDisable(false);
        }
    }

    @FXML 
    public void onPriceSorting() {
        this.hotel.sortByPrice();
        this.availableRoomsShowing.getItems().clear();
        this.availableRoomsShowing.getItems().setAll(this.hotel.getAvailableRooms());
        
        this.checkIfNoItemSelected();
    }

    @FXML 
    public void onRoomNumberSorting() {
        this.hotel.sortByRoomNumber();
        this.availableRoomsShowing.getItems().clear();
        this.availableRoomsShowing.getItems().setAll(this.hotel.getAvailableRooms());
        
        this.checkIfNoItemSelected();
    }

    @FXML
    public void onChosenRoom() {
        if (this.availableRoomsShowing.getSelectionModel().getSelectedItem() != null) this.continueToBooking.setDisable(false);
    }

    @FXML
    public void onContinueToBooking() {
        Room chosenRoom = this.availableRoomsShowing.getSelectionModel().getSelectedItem();
        int daysOfStay = hotel.getNumOfDaysForStay(this.checkInDate.getValue(), this.checkOutDate.getValue());

    //! Setter feltene for valgt rom, antall dager og prisen
        if (chosenRoom.hasSpecialName()){
            this.chosenRoomNumberForStay.setText(""+chosenRoom.getSpecialName());
        }
        else {
            this.chosenRoomNumberForStay.setText(""+chosenRoom.getRoomNumber());
        }
        this.numberOfDaysToStay.setText("" + daysOfStay);
        this.totalPriceForStay.setText("" + Hotel.calculatePrice(chosenRoom.getRoomPrice(), daysOfStay) + "kr");
        
        this.onClickChangeView();
        this.disableBookButtonWhileFieldsAreEmpty();
    }


    @FXML
    public void onClickCheckDate() {
        LocalDate wantedFromDate = this.checkInDate.getValue();
        LocalDate wantedToDate = this.checkOutDate.getValue();
  
        this.availableRoomsShowing.getItems().clear();
   
        try {
            hotel.updateAvailableRoomsForTimeframe(wantedFromDate, wantedToDate);
            this.illegalDate.setVisible(false);  
        }
        catch (IllegalArgumentException e) {
            this.illegalDate.setVisible(true);
        }
        
        this.availableRoomsShowing.getItems().addAll(hotel.getAvailableRooms());
        
        this.checkIfNoItemSelected();
        
        this.sortByPrice.setDisable(false);
        this.sortByRoomNumber.setDisable(false);
    }

    @FXML
    public void onBookRoom() {
        Room chosenRoom = this.availableRoomsShowing.getSelectionModel().getSelectedItem();
        
        try {
            ValidatedPerson p1 = new ValidatedPerson(this.inputFirstName.getText(),
                this.inputLastName.getText(), Integer.parseInt(this.inputAge.getText()), 
                this.inputGender.getText(), this.inputEmail.getText(), this.inputFNr.getText());
            this.hotel.bookRoomInHotel(chosenRoom, p1, this.checkInDate.getValue(), this.checkOutDate.getValue());
            this.illegalPerson.setVisible(false);
        }
        catch (IllegalArgumentException e) {
        //! Vil vise feilmelding for bookinginfo
            this.illegalPerson.setVisible(true);
            return;
        }
        
        this.setVisibilityAndAbilityForPage(this.frontPageView, false);
        this.setVisibilityAndAbilityForPage(this.endScreen, true);

        try {
            FileHelper.writeStateToFile("c:/Users/kraa1/Documents/OvingerOgSlikt/tdt4100-students-23/TDT4100-prosjekt-kristtaa/src/main/resources/hotelbooking/hotelStateFile.txt", 
                hotel);
            System.out.println("Skrevet til fil");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickChangeView () {
        //! Bytte til bookingside
        if (showingFrontPage) {
            this.setVisibilityAndAbilityForPage(this.frontPageView, false);     
            this.setVisibilityAndAbilityForPage(this.bookingPageView, true);
            showingFrontPage = false;

            System.out.println("hei");
        }

        //! Bytte til forside
        else {
            this.setVisibilityAndAbilityForPage(this.frontPageView, true);
            this.setVisibilityAndAbilityForPage(this.bookingPageView, false);
            showingFrontPage = true;
        }
    }

    @FXML
    public void onClickGoBackFromEndScreen () {
        // Bytte til forside
        this.setVisibilityAndAbilityForPage(this.endScreen, false);
        this.setVisibilityAndAbilityForPage(this.bookingPageView, false);
        this.setVisibilityAndAbilityForPage(this.frontPageView, true);

               

        showingFrontPage = true;
        this.onClickCheckDate();

        this.clearAllInputFields();

        this.continueToBooking.setDisable(true);
        this.bookRoom.setDisable(true);
        
    }

    private void clearAllInputFields() {
        this.allInputFields.forEach(field -> field.clear());
    }

    //! Når man sorterer rommene, velger ny dato eller kommer tilbake fra endScreen skal man ikke kunne gå videre uten å ha 
//! valgt et rom
    private void checkIfNoItemSelected(){
        if (this.availableRoomsShowing.getSelectionModel().getSelectedItem() == null) {
            this.continueToBooking.setDisable(true);
        }
    }

     


}   
