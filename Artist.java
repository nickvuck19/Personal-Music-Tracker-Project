

import java.util.*;

public class Artist {
    private int id;
    /** Name of the artist*/
    private String name;
    /** Name of what musical genre the artist makes music in*/
    private String genre;
    /** An optional description of the musical artist, something like a bio*/
    private String description;
    /** A list of albums by the artist*/
    private List<Album> albums;
    /** A list of songs by the artist */
    private List<Song> songs;

    public Artist(int id, String name, String genre, String description){
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.description = description;
        this.albums = new ArrayList<>();
    }

    // Getters and Setters
    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getGenre(){
        return this.genre;
    }

    public String getDescription(){
        return this.description;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public void setDescription(String description){
        this.description = description;
    }

    // Add an Album to the Artist's Collection
    public void addAlbum(Album album){
        if (!albums.contains(album)){
            albums.add(album);
            System.out.println("Album added: " + album.getTitle());
        } else{
            System.out.println("Album already exists.");
        }
    }

    // Remove an Album
    public void removeAlbum(Album album){
        if(albums.remove(album)){
            System.out.println("Album removed: " + album.getTitle());
        } else{
            System.out.println("Album not found.");
        }
    }
}
