package com.musiclibrary.dao;

import com.musiclibrary.db.DBConnection;
import com.musiclibrary.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {

    // ── map a ResultSet row → Song ────────────────────────────────────────────
    private Song map(ResultSet rs) throws SQLException {
        Song s = new Song(
            rs.getInt("album_id"),
            rs.getString("title"),
            rs.getString("artist"),
            rs.getString("duration"),
            rs.getString("genre"),
            rs.getBoolean("liked")
        );
        s.setSongId(rs.getInt("song_id"));
        return s;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addSong(Song s) {
        String sql = "INSERT INTO songs (album_id,title,artist,duration,genre,liked) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getAlbumId());
            ps.setString(2, s.getTitle());
            ps.setString(3, s.getArtist());
            ps.setString(4, s.getDuration());
            ps.setString(5, s.getGenre());
            ps.setBoolean(6, s.isLiked());
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ── READ ALL ──────────────────────────────────────────────────────────────
    public List<Song> getAllSongs() {
        return fetch("SELECT * FROM songs ORDER BY song_id");
    }

    // ── READ LIKED ────────────────────────────────────────────────────────────
    public List<Song> getLikedSongs() {
        return fetch("SELECT * FROM songs WHERE liked = TRUE ORDER BY title");
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────
    public List<Song> search(String keyword) {
        List<Song> list = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE LOWER(title) LIKE ? OR LOWER(artist) LIKE ? OR LOWER(genre) LIKE ? ORDER BY title";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, k); ps.setString(2, k); ps.setString(3, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ── SORT ──────────────────────────────────────────────────────────────────
    public List<Song> getSorted(String col) {
        // whitelist to prevent SQL injection
        String safe = switch (col) {
            case "title", "artist", "duration", "genre", "album_id" -> col;
            default -> "title";
        };
        return fetch("SELECT * FROM songs ORDER BY " + safe);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean updateSong(Song s) {
        String sql = "UPDATE songs SET album_id=?,title=?,artist=?,duration=?,genre=?,liked=? WHERE song_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getAlbumId());
            ps.setString(2, s.getTitle());
            ps.setString(3, s.getArtist());
            ps.setString(4, s.getDuration());
            ps.setString(5, s.getGenre());
            ps.setBoolean(6, s.isLiked());
            ps.setInt(7, s.getSongId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ── TOGGLE LIKED ─────────────────────────────────────────────────────────
    public boolean toggleLiked(int id, boolean liked) {
        String sql = "UPDATE songs SET liked=? WHERE song_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, liked); ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean deleteSong(int id) {
        String sql = "DELETE FROM songs WHERE song_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ── helper ────────────────────────────────────────────────────────────────
    private List<Song> fetch(String sql) {
        List<Song> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
