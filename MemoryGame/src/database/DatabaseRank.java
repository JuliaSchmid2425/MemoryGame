package database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DatabaseRank {

    private static final String TOP_PLAYERS_QUERY =
            "SELECT jugadors.username, partides.punts, partides.errors, partides.durada_partida " +
                    "FROM jugadors JOIN partides ON jugadors.id = partides.id_jugador " +
                    "ORDER BY partides.punts DESC LIMIT 10";

    public static void showTopPlayers() {
        JFrame frame = new JFrame("Top 10 jugadors per punts");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Crear el model de taula
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Username");
        model.addColumn("Punts");
        model.addColumn("Errors");
        model.addColumn("Durada (s)");

        JTable table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Carregar dades des de la base de dades
        try (Connection con = DriverManager.getConnection(
                DatabaseManager.dataBaseURL,
                DatabaseManager.user,
                DatabaseManager.password);
             PreparedStatement ps = con.prepareStatement(TOP_PLAYERS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getInt("punts"),
                        rs.getInt("errors"),
                        rs.getInt("durada_partida")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,
                    "Error carregant dades: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    // Per provar-ho des d'aquí directament
    public static void main(String[] args) {
        showTopPlayers();
    }
}
