

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MusicTracker {
    private List<Artist> artists;
    private List<Album> albums;
    private List<Song> songs;

    // List for Liked Songs
    private List<Song> likedSongs = new ArrayList<>();

    public MusicTracker(){
        this.artists = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.songs = new ArrayList<>();
    }

    // Add a new artist
    public void addArtist(Artist artist){
        artists.add(artist);
    }

    // Add a new album
    public void addAlbum(Album album){
        albums.add(album);
    }

    // Add a new song
    public void addSong(Song song){
        songs.add(song);
    }

    // Find a song by its title
    public Song findSongByTitle(String title){
        for (Song song : songs){
            if(song.getTitle().equalsIgnoreCase(title)){
                return song;
            }
        }
        return null; // song not found
    }

    // Add a song to the liked songs list
    public void addLikedSong(Song song){

        // Check if the song already exists in the likedSongs list
        for (Song exists : likedSongs){
            if (exists.getTitle().equalsIgnoreCase(song.getTitle()) &&
                exists.getArtist().getName().equalsIgnoreCase(song.getArtist().getName()) &&
                exists.getAlbum().getTitle().equalsIgnoreCase(song.getAlbum().getTitle())) {
                System.out.println("This song has already been added to the liked list: " + song.getTitle());
                return;
            }
        }
        likedSongs.add(song);
        System.out.println("Song added to liked list: " + song.getTitle());
    }

    // Save liked songs to CSV
    public void saveLikedSongsToCSV(String fileName){
        try(FileWriter writer = new FileWriter(fileName)){
            writer.write("song | artist | album\n");

            // HashSet to keep track of unique songs
            Set<String> songTitles = new HashSet<>();

            for (Song song : likedSongs){
                String songIdentifier = song.getTitle() + "|" + song.getArtist().getName() + "|" +
                                        song.getAlbum().getTitle();
                if(!songTitles.contains(songIdentifier)) {
                    writer.write(song.getTitle() + "," + song.getArtist().getName() + "," +
                            song.getAlbum().getTitle() + "\n");
                    songTitles.add(songIdentifier);
                }
            }
            System.out.println("Liked songs saved to " + fileName);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadLikedSongsFromCSV(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("song |")) continue; // Skip the header row
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String songTitle = parts[0].trim();
                    String artistName = parts[1].trim();
                    String albumTitle = parts[2].trim();

                    // Find or create Artist and Album
                    Artist artist = new Artist(0, artistName, "Unknown", ""); // Placeholder
                    Album album = new Album(0, albumTitle, 2020, artist); // Placeholder year

                    // Create the new Song object and add it to the liked list
                    Song newSong = new Song(0, songTitle, artist, album, "Pop"); // Placeholder genre
                    likedSongs.add(newSong);
                }
            }
            System.out.println("Liked songs loaded from CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Artist getArtistByName(String artistName){
        for (Artist artist : artists){
            if (artist.getName().equals(artistName)){
                return artist;
            }
        }
        return null;
    }

    private Album getAlbumByTitle(String albumTitle){
        for (Album album : albums){
            if(album.getTitle().equals(albumTitle)){
                return album;
            }
        }
        return null;
    }

    // Getter for liked songs
    public List<Song> getLikedSongs() {
        return likedSongs;
    }

    // Print all songs
    public void printAllSongs(){
        for (Song song : songs){
            song.printDetails();
        }
    }

    // Print all artists
    public void printAllArtists(){
        for (Artist artist : artists){
            System.out.println(artist.getName());
        }
    }
}
