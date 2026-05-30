package com.musiclibrary.model;

public class Song {
    private int     songId;
    private int     albumId;
    private String  title;
    private String  artist;
    private String  duration;
    private String  genre;
    private boolean liked;

    // Constructor for INSERT (no id yet)
    public Song(int albumId, String title, String artist, String duration, String genre, boolean liked) {
        this.albumId  = albumId;
        this.title    = title;
        this.artist   = artist;
        this.duration = duration;
        this.genre    = genre;
        this.liked    = liked;
    }

    // Getters
    public int     getSongId()   { return songId;   }
    public int     getAlbumId()  { return albumId;  }
    public String  getTitle()    { return title;    }
    public String  getArtist()   { return artist;   }
    public String  getDuration() { return duration; }
    public String  getGenre()    { return genre;    }
    public boolean isLiked()     { return liked;    }

    // Setters
    public void setSongId(int songId)       { this.songId   = songId;   }
    public void setAlbumId(int albumId)     { this.albumId  = albumId;  }
    public void setTitle(String title)      { this.title    = title;    }
    public void setArtist(String artist)    { this.artist   = artist;   }
    public void setDuration(String d)       { this.duration = d;        }
    public void setGenre(String genre)      { this.genre    = genre;    }
    public void setLiked(boolean liked)     { this.liked    = liked;    }
}
