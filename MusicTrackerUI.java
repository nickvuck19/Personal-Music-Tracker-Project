
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;



public class MusicTrackerUI extends Application {

    private MusicTracker tracker = new MusicTracker();

    private TextField songTitleField = new TextField();
    private TextField artistNameField = new TextField();
    private TextField albumTitleField = new TextField();
    private TableView<Song> likedSongsTable = new TableView<>();
    private ObservableList<Song> likedSongsData = FXCollections.observableArrayList();
    private Label songCountLabel = new Label("Total Songs: 0");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){

        tracker.loadLikedSongsFromCSV("liked_songs.csv"); // load saved songs
        likedSongsData.addAll(tracker.getLikedSongs()); // Populate table with data

        updateSongCount();

        // Create a layout for the UI
        VBox layout = new VBox(10);

        // Add labels and text fields to the layout
        layout.getChildren().addAll(
                new Label("Song Title:"), songTitleField,
                new Label("Artist name:"), artistNameField,
                new Label("Album Title"), albumTitleField,
                createAddSongButton(),
                createRemoveButton(),
                new Label("Liked Songs List:"),
                createTableView(),
                songCountLabel
        );

        // Set up the scene and stage
        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.setTitle("Music Tracker");
        stage.show();
    }

    // Create the TableView
    private TableView<Song> createTableView(){

        TableColumn<Song, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("Selected"));

        // Use a CheckBox in each row to select songs
        selectColumn.setCellFactory(col -> new TableCell<Song, Boolean>() {
            private final CheckBox cb = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                } else{
                    setGraphic(cb);
                    cb.setSelected(item != null && item);

                    cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if(getTableRow() != null && getTableRow().getItem() != null){
                            getTableRow().getItem().setSelected(newValue);
                        }
                    });
                }
            }
        });

        TableColumn<Song, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
        artistColumn.setCellValueFactory(data -> data.getValue().getArtist() != null ?
                new javafx.beans.property.SimpleStringProperty(data.getValue().getArtist().getName()) :
                new javafx.beans.property.SimpleStringProperty("Unknown"));

        TableColumn<Song, String> albumColumn = new TableColumn<>("Album");
        albumColumn.setCellValueFactory(data -> data.getValue().getAlbum() != null ?
                new javafx.beans.property.SimpleStringProperty(data.getValue().getAlbum().getTitle()) :
                new javafx.beans.property.SimpleStringProperty("Unknown"));

        // Enable sorting by column
        titleColumn.setSortable(true);
        artistColumn.setSortable(true);
        albumColumn.setSortable(true);

        likedSongsTable.getColumns().addAll(selectColumn, titleColumn, artistColumn, albumColumn);
        likedSongsTable.setItems(likedSongsData);

        return likedSongsTable;
    }

    // Method to sort by title
    private void sortByTitle(){
        likedSongsData.sort((song1, song2) -> song1.getTitle().compareToIgnoreCase(song2.getTitle()));
    }

    // Method to sort by Artist
    private void sortByArtist(){
        likedSongsData.sort((song1, song2) -> song1.getArtist().getName().compareToIgnoreCase(
                song2.getArtist().getName()));
    }

    // method to sort by Album
    private void sortByAlbum(){
        likedSongsData.sort((song1, song2) -> song1.getAlbum().getTitle().compareToIgnoreCase(
                song2.getAlbum().getTitle()));
    }

    // Create the button to add a song
    private Button createAddSongButton(){
        Button addButton = new Button("Add Song");
        addButton.setOnAction(event -> addSongToLikedList());
        return addButton;
    }

    // Create Sorting Buttons
    private VBox createSortButtons(){
        Button sortByTitleButton = new Button("Sort by Title");
        sortByTitleButton.setOnAction(e -> sortByTitle());

        Button sortByArtistButton = new Button("Sort by Artist");
        sortByArtistButton.setOnAction(e -> sortByArtist());

        Button sortByAlbumButton = new Button("Sort By Album");
        sortByAlbumButton.setOnAction(e -> sortByAlbum());

        VBox sortButtonBox = new VBox(5, sortByTitleButton, sortByArtistButton, sortByAlbumButton);
        return sortButtonBox;
    }

    // Create Remove Button
    private Button createRemoveButton(){
        Button removeButton = new Button("Remove Selected Songs");
        removeButton.setOnAction(event -> removeSelectedSongs());
        return removeButton;
    }

    // Method to remove selected songs from table and CSV
    private void removeSelectedSongs(){
        tracker.getLikedSongs().removeIf(song -> song.isSelected());
        likedSongsData.removeIf(song -> song.isSelected());
        tracker.saveLikedSongsToCSV("liked_songs.csv");
        updateSongCount();

        showAlert("Success", "Selected songs have been removed.");
    }

    // Method to update the song count display
    private void updateSongCount(){
        songCountLabel.setText("Total Songs: " + likedSongsData.size());
    }

    // Method to add the song to the liked list and save to CSV file
    private void addSongToLikedList(){
        String title = songTitleField.getText();
        String artist = artistNameField.getText();
        String album = albumTitleField.getText();

        if(title.isEmpty() || artist.isEmpty() || album.isEmpty()){
            showAlert("Input Error", "All fields must be filled out");
            return;
        }

        // Find or create the artist and album
        Artist artistObj = new Artist(0, artist, "Unknown", ""); // Using placeholder info
        Album albumObj = new Album(0, album, 2020, artistObj); // Using a placeholder year

        // Create the new song and add it to the liked songs list
        Song newSong = new Song(0, title, artistObj, albumObj, "Metal");

        // Check for duplicates before adding
        if(tracker.getLikedSongs().stream().anyMatch(s -> s.getTitle().equalsIgnoreCase(title) &&
                s.getArtist().getName().equalsIgnoreCase(artist))){
            showAlert("Duplicate Song", "This song is already in your liked list.");
            return;
        }

        tracker.addLikedSong(newSong);
        likedSongsData.add(newSong); // Update TableView

        // Save the updated liked songs to CSV file
        tracker.saveLikedSongsToCSV("liked_songs.csv");

        updateSongCount();

        // Clear the input fields
        songTitleField.clear();
        artistNameField.clear();
        albumTitleField.clear();
    }

    // Update the ListView to reflect the liked songs list
   // private void updateLikedSongsListView(){
   //     likedSongsListView.getItems().clear();
    //    for (Song song : tracker.getLikedSongs()) {
    //        likedSongsListView.getItems().add(song.getTitle() + " by " + song.getArtist().getName() + "-" +
    //                song.getAlbum().getTitle());
   //     }
  //  }

    // Show an alert for input validation
    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
