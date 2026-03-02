import java.time.LocalDate;
import javafx.beans.property.*;

public class Song {
    private int id;
    private String title;
    private Artist artist;
    private Album album;
    private String genre;
    private int playCount;
    private LocalDate addedDate = LocalDate.now();
    private int rating;
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    public Song(int id, String title){
        this.id = id;
        this.title = title;
        this.playCount = 0;
    }

    public Song(int id, String title, Artist artist, Album album, String genre){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.playCount = 0; // defaults to 0 plays
    }

    public BooleanProperty selectedProperty(){
        return selected;
    }

    public boolean isSelected(){
        return selected.get();
    }

    public void setSelected(boolean selected){
        this.selected.set(selected);
    }

    // Getters and setters
    public int getId(){ return id; }
    public String getTitle(){ return title; }
    public Artist getArtist(){ return artist; }
    public Album getAlbum(){ return album; }
    public String getGenre(){ return genre; }
    public int getPlayCount() { return playCount; }
    public void setRating(int rating){ if (rating >= 1 && rating <= 5) this.rating = rating; }

    // Setters for artist and album
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    // Increment playCount when a song is played
    public void playSong(){
        this.playCount++;
        System.out.println("Playing: " + title + " by " + artist.getName() + " (Play count: " + playCount + ")");
    }

    // Display song details
    public void printDetails(){
        System.out.println("Song: " + title);
        System.out.println("Artist: " + artist.getName());
        System.out.println("Album: " + (album != null ? album.getTitle() : "Unknown Album"));
        System.out.println("Genre: " + genre);
        System.out.println("Play Count " + playCount);
    }
}
