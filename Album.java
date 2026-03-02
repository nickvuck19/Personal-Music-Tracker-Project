

import java.util.*;

public class Album {
    private int id;
    private String title;
    private int releaseYear;
    private Artist artist;
    private List<Song> songs;

    public Album(int id, String title, int releaseYear, Artist artist){
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.songs = new ArrayList<>();
    }
    // Getters and Setters
    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public int getReleaseYear(){
        return this.releaseYear;
    }

    public Artist getArtist(){
        return this.artist;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setReleaseYear(int releaseYear){
        this.releaseYear = releaseYear;
    }

    public void addSongByName(int id, String songName){
        Song song = new Song(id, songName);
        song.setArtist(this.artist);
        song.setAlbum(this);
        songs.add(song);
        System.out.println("Song added: " + songName);
    }

    public void printAlbumDetails(){
        System.out.println("Album " + title);
        System.out.println("Release Year: " + releaseYear);
        for (Song song: songs){
            song.printDetails();
        }
    }
}
