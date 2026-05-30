package com.musiclibrary.ui;

import com.musiclibrary.dao.SongDAO;
import com.musiclibrary.model.Song;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainFrame extends JFrame {

    /* ── colour palette ─────────────────────────────────────────────────── */
    private static final Color C_BG       = new Color(15, 15, 25);
    private static final Color C_SIDEBAR  = new Color(22, 22, 38);
    private static final Color C_CARD     = new Color(32, 32, 52);
    private static final Color C_CARD2    = new Color(40, 40, 62);
    private static final Color C_ACCENT   = new Color(138, 43, 226);   // purple
    private static final Color C_ACCENT2  = new Color(0, 191, 255);    // cyan
    private static final Color C_TEXT     = new Color(235, 235, 255);
    private static final Color C_MUTED    = new Color(130, 130, 160);
    private static final Color C_LIKED    = new Color(255, 65, 105);
    private static final Color C_GREEN    = new Color(50, 205, 100);
    private static final Color C_RED      = new Color(255, 75, 75);

    /* ── fonts ──────────────────────────────────────────────────────────── */
    private static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font F_LABEL  = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_FIELD  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BTN    = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_TABLE  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_THEAD  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);

    /* ── state ──────────────────────────────────────────────────────────── */
    private final SongDAO dao = new SongDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField tfTitle, tfArtist, tfDuration, tfAlbum, tfGenre, tfSearch;
    private JLabel     lblStatus, lblCount, lblNowPlaying;
    private JButton    btnAdd, btnUpdate, btnDelete, btnLike, btnClear,
                       btnSearch, btnShowAll, btnShowLiked;
    private JComboBox<String> cbSort;

    private boolean viewingLiked = false;

    /* ── constructor ────────────────────────────────────────────────────── */
    public MainFrame() {
        setTitle("Music Library Manager");
        setSize(1200, 740);
        setMinimumSize(new Dimension(1000, 620));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout());

        add(makeHeader(),    BorderLayout.NORTH);
        add(makeBody(),      BorderLayout.CENTER);
        add(makeStatusBar(), BorderLayout.SOUTH);

        wireEvents();
        refresh(dao.getAllSongs());
        setVisible(true);
    }

    /* ══════════════════════════════════════════════════════════════════════
       HEADER
    ══════════════════════════════════════════════════════════════════════ */
    private JPanel makeHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_SIDEBAR);
        p.setBorder(new EmptyBorder(16, 24, 16, 24));

        // left – logo
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JLabel icon = new JLabel("♫");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 30));
        icon.setForeground(C_ACCENT);
        JLabel title = new JLabel("Music Library Manager");
        title.setFont(F_TITLE);
        title.setForeground(C_TEXT);
        left.add(icon); left.add(title);

        // right – now playing
        lblNowPlaying = new JLabel("No song selected");
        lblNowPlaying.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNowPlaying.setForeground(C_MUTED);

        p.add(left,          BorderLayout.WEST);
        p.add(lblNowPlaying, BorderLayout.EAST);
        return p;
    }

    /* ══════════════════════════════════════════════════════════════════════
       BODY  (left form | right table)
    ══════════════════════════════════════════════════════════════════════ */
    private JPanel makeBody() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(12, 16, 8, 16));
        p.add(makeFormPanel(),  BorderLayout.WEST);
        p.add(makeTablePanel(), BorderLayout.CENTER);
        return p;
    }

    /* ── LEFT: form ─────────────────────────────────────────────────────── */
    private JPanel makeFormPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(C_SIDEBAR);
        outer.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(C_CARD2, 1, true),
            new EmptyBorder(18, 18, 18, 18)
        ));
        outer.setPreferredSize(new Dimension(290, 0));

        // heading
        JLabel h = new JLabel("  Song Details");
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        h.setForeground(C_ACCENT2);
        h.setBorder(new EmptyBorder(0, 0, 6, 0));

        // fields
        tfTitle    = field("e.g.  Kesariya");
        tfArtist   = field("e.g.  Arijit Singh");
        tfDuration = field("e.g.  4:28");
        tfAlbum    = field("e.g.  1");
        tfGenre    = field("e.g.  Bollywood");

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1; g.gridx = 0;
        g.insets = new Insets(3, 0, 3, 0);

        String[] labels = {"Title *", "Artist *", "Duration *", "Album ID *", "Genre"};
        JTextField[] fields = {tfTitle, tfArtist, tfDuration, tfAlbum, tfGenre};
        for (int i = 0; i < labels.length; i++) {
            g.gridy = i * 2;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(F_LABEL); lbl.setForeground(C_MUTED);
            form.add(lbl, g);
            g.gridy = i * 2 + 1;
            form.add(fields[i], g);
        }

        // buttons 2x3 grid
        JPanel btns = new JPanel(new GridLayout(3, 2, 8, 8));
        btns.setOpaque(false);
        btns.setBorder(new EmptyBorder(14, 0, 0, 0));

        btnAdd    = btn("➕  Add Song",   C_ACCENT);
        btnUpdate = btn("✏  Update",      C_ACCENT2);
        btnDelete = btn("🗑  Delete",      C_RED);
        btnLike   = btn("♡  Favourite",   C_LIKED);
        btnClear  = btn("✖  Clear",       C_CARD2);

        btns.add(btnAdd);    btns.add(btnUpdate);
        btns.add(btnDelete); btns.add(btnLike);
        btns.add(btnClear);  btns.add(new JLabel()); // spacer

        outer.add(h,    BorderLayout.NORTH);
        outer.add(form, BorderLayout.CENTER);
        outer.add(btns, BorderLayout.SOUTH);
        return outer;
    }

    /* ── RIGHT: toolbar + table ─────────────────────────────────────────── */
    private JPanel makeTablePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(C_BG);

        p.add(makeToolbar(), BorderLayout.NORTH);
        p.add(makeTable(),   BorderLayout.CENTER);
        return p;
    }

    private JPanel makeToolbar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setBackground(C_BG);

        tfSearch = field("Search title / artist / genre…");
        tfSearch.setPreferredSize(new Dimension(240, 36));

        btnSearch   = btn("🔍  Search",      C_ACCENT);
        btnShowAll  = btn("📋  All Songs",    C_CARD2);
        btnShowLiked= btn("❤  Favourites",   C_LIKED);

        cbSort = new JComboBox<>(new String[]{
            "Sort by…", "Title", "Artist", "Duration", "Genre", "Album ID"
        });
        styleCombo(cbSort);

        lblCount = new JLabel("0 songs");
        lblCount.setFont(F_SMALL);
        lblCount.setForeground(C_MUTED);

        p.add(tfSearch); p.add(btnSearch);
        p.add(Box.createHorizontalStrut(8));
        p.add(btnShowAll); p.add(btnShowLiked);
        p.add(Box.createHorizontalStrut(8));
        p.add(cbSort);
        p.add(Box.createHorizontalStrut(12));
        p.add(lblCount);
        return p;
    }

    private JScrollPane makeTable() {
        String[] cols = {"ID", "Album", "Title", "Artist", "Duration", "Genre", "♥"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int c) {
                return (c == 6) ? Boolean.class : String.class;
            }
        };

        table = new JTable(tableModel);
        table.setFont(F_TABLE);
        table.setBackground(C_CARD);
        table.setForeground(C_TEXT);
        table.setSelectionBackground(new Color(90, 40, 160));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.setFillsViewportHeight(true);

        // header
        JTableHeader th = table.getTableHeader();
        th.setFont(F_THEAD);
        th.setBackground(new Color(28, 20, 55));
        th.setForeground(C_ACCENT2);
        th.setBorder(BorderFactory.createEmptyBorder());
        th.setReorderingAllowed(false);

        // column widths
        int[] w = {45, 60, 220, 170, 80, 100, 45};
        for (int i = 0; i < w.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        // cell renderer – alternating rows
        DefaultTableCellRenderer cellR = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setFont(F_TABLE);
                setForeground(C_TEXT);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!sel) setBackground(row % 2 == 0 ? C_CARD : C_CARD2);
                return this;
            }
        };
        table.setDefaultRenderer(Object.class, cellR);
        table.setDefaultRenderer(String.class, cellR);

        // liked column renderer
        table.getColumnModel().getColumn(6).setCellRenderer(
            new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(
                        JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                    boolean liked = (v instanceof Boolean) && (Boolean) v;
                    JLabel lbl = new JLabel(liked ? "❤" : "♡", SwingConstants.CENTER);
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 17));
                    lbl.setForeground(liked ? C_LIKED : C_MUTED);
                    lbl.setOpaque(true);
                    lbl.setBackground(sel ? new Color(90,40,160) : row%2==0 ? C_CARD : C_CARD2);
                    return lbl;
                }
            }
        );

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(C_CARD);
        sp.setBorder(BorderFactory.createLineBorder(C_CARD2, 1));
        return sp;
    }

    /* ── STATUS BAR ─────────────────────────────────────────────────────── */
    private JPanel makeStatusBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 5));
        p.setBackground(C_SIDEBAR);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_CARD2));
        lblStatus = new JLabel("Ready");
        lblStatus.setFont(F_SMALL);
        lblStatus.setForeground(C_MUTED);
        p.add(lblStatus);
        return p;
    }

    /* ══════════════════════════════════════════════════════════════════════
       EVENTS
    ══════════════════════════════════════════════════════════════════════ */
    private void wireEvents() {

        // row click → fill form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillForm();
        });

        // ADD
        btnAdd.addActionListener(e -> {
            Song s = buildSong(0);
            if (s == null) return;
            if (dao.addSong(s)) { status("Song added!", C_GREEN); clearForm(); reload(); }
            else                  status("Failed to add song.", C_RED);
        });

        // UPDATE
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { status("Select a song to update.", C_RED); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            Song s = buildSong(id);
            if (s == null) return;
            if (dao.updateSong(s)) { status("Song updated!", C_GREEN); reload(); }
            else                     status("Update failed.", C_RED);
        });

        // DELETE
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { status("Select a song to delete.", C_RED); return; }
            String t = tableModel.getValueAt(row, 2).toString();
            int id   = (int) tableModel.getValueAt(row, 0);
            int ok   = JOptionPane.showConfirmDialog(this,
                "Delete  \"" + t + "\" ?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                dao.deleteSong(id);
                status("Deleted: " + t, C_RED);
                clearForm(); reload();
            }
        });

        // LIKE / UNLIKE
        btnLike.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { status("Select a song first.", C_RED); return; }
            int     id    = (int)     tableModel.getValueAt(row, 0);
            boolean liked = (Boolean) tableModel.getValueAt(row, 6);
            dao.toggleLiked(id, !liked);
            status(liked ? "Removed from favourites." : "Added to favourites ❤", liked ? C_MUTED : C_LIKED);
            reload();
        });

        // CLEAR
        btnClear.addActionListener(e -> clearForm());

        // SEARCH
        btnSearch.addActionListener(e -> {
            String kw = tfSearch.getText().trim();
            if (kw.isEmpty()) { reload(); return; }
            refresh(dao.search(kw));
            status("Results for: " + kw, C_ACCENT2);
        });
        tfSearch.addActionListener(e -> btnSearch.doClick());

        // SHOW ALL
        btnShowAll.addActionListener(e -> {
            viewingLiked = false;
            tfSearch.setText("");
            reload();
            status("Showing all songs.", C_MUTED);
        });

        // SHOW LIKED
        btnShowLiked.addActionListener(e -> {
            viewingLiked = true;
            refresh(dao.getLikedSongs());
            status("Showing favourites ❤", C_LIKED);
        });

        // SORT
        cbSort.addActionListener(e -> {
            String sel = (String) cbSort.getSelectedItem();
            if (sel == null || sel.startsWith("Sort")) return;
            String col = switch (sel) {
                case "Title"    -> "title";
                case "Artist"   -> "artist";
                case "Duration" -> "duration";
                case "Genre"    -> "genre";
                case "Album ID" -> "album_id";
                default         -> "title";
            };
            refresh(dao.getSorted(col));
            status("Sorted by " + sel, C_ACCENT2);
        });
    }

    /* ══════════════════════════════════════════════════════════════════════
       HELPERS
    ══════════════════════════════════════════════════════════════════════ */
    private void refresh(List<Song> songs) {
        tableModel.setRowCount(0);
        for (Song s : songs) {
            tableModel.addRow(new Object[]{
                s.getSongId(), s.getAlbumId(), s.getTitle(),
                s.getArtist(), s.getDuration(), s.getGenre(), s.isLiked()
            });
        }
        lblCount.setText(songs.size() + (songs.size() == 1 ? " song" : " songs"));
    }

    private void reload() {
        refresh(viewingLiked ? dao.getLikedSongs() : dao.getAllSongs());
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        tfAlbum.setText(tableModel.getValueAt(row, 1).toString());
        tfTitle.setText(tableModel.getValueAt(row, 2).toString());
        tfArtist.setText(tableModel.getValueAt(row, 3).toString());
        tfDuration.setText(tableModel.getValueAt(row, 4).toString());
        tfGenre.setText(tableModel.getValueAt(row, 5).toString());
        boolean liked = (Boolean) tableModel.getValueAt(row, 6);
        btnLike.setText(liked ? "💔  Unfavourite" : "♡  Favourite");
        lblNowPlaying.setText("Selected:  " + tableModel.getValueAt(row, 2));
    }

    private Song buildSong(int id) {
        String title = tfTitle.getText().trim();
        String artist = tfArtist.getText().trim();
        String dur    = tfDuration.getText().trim();
        String albStr = tfAlbum.getText().trim();
        String genre  = tfGenre.getText().trim();

        if (title.isEmpty() || artist.isEmpty() || dur.isEmpty() || albStr.isEmpty()) {
            status("Title, Artist, Duration and Album ID are required.", C_RED);
            return null;
        }
        int albumId;
        try { albumId = Integer.parseInt(albStr); }
        catch (NumberFormatException ex) {
            status("Album ID must be a number.", C_RED); return null;
        }
        Song s = new Song(albumId, title, artist, dur, genre.isEmpty() ? "Unknown" : genre, false);
        if (id > 0) s.setSongId(id);
        return s;
    }

    private void clearForm() {
        tfTitle.setText(""); tfArtist.setText(""); tfDuration.setText("");
        tfAlbum.setText(""); tfGenre.setText("");
        btnLike.setText("♡  Favourite");
        lblNowPlaying.setText("No song selected");
        table.clearSelection();
    }

    private void status(String msg, Color c) {
        lblStatus.setText(msg);
        lblStatus.setForeground(c);
    }

    /* ══════════════════════════════════════════════════════════════════════
       WIDGET FACTORIES
    ══════════════════════════════════════════════════════════════════════ */
    private JTextField field(String hint) {
        JTextField tf = new JTextField() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g.setColor(C_MUTED);
                    g.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                    g.drawString(hint, 8, getHeight() / 2 + 4);
                }
            }
        };
        tf.setFont(F_FIELD);
        tf.setBackground(C_CARD);
        tf.setForeground(C_TEXT);
        tf.setCaretColor(C_ACCENT2);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(65, 65, 100), 1, true),
            new EmptyBorder(7, 9, 7, 9)
        ));
        return tf;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed() ? bg.darker().darker()
                           : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(F_BTN);
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(9, 16, 9, 16));
        return b;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(F_FIELD);
        cb.setBackground(C_CARD);
        cb.setForeground(C_TEXT);
        cb.setFocusable(false);
        cb.setBorder(BorderFactory.createLineBorder(new Color(65, 65, 100), 1));
    }
}
