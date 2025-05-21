package database;

import java.sql.*;

public class DatabaseManager {


    private static String dataBaseURL = "jdbc:mysql://localhost:3306/gamememory"; //posar nom bdd
    private static String user = "julia"; //canviar user
    private static String password = "julia"; //canviar psw

    //primer cal crear taula
    //indicar taula. cada 'interrogacio' es una columna definida a ps.set
    private static String insertTableJugadors = "insert into jugadors (username) values (?)";
    private static String insertTablePartides = "insert into partides (id_jugador, punts, `errors`, durada_partida, hora_partida) values (?, ?, ?, ?, ?)";



    public static void saveGame(String username, int points, int errors, int duration) {

        int jugadorId; //guardar id jugador

        try {
            Connection con = DriverManager.getConnection(dataBaseURL, user, password);

            //insereix username i recupera la clau (id) generat automaticament
            try (PreparedStatement psInsertJugador = con.prepareStatement(insertTableJugadors, Statement.RETURN_GENERATED_KEYS)) {
                psInsertJugador.setString(1, username); //username
                psInsertJugador.executeUpdate(); //inserta a BBDD

                //
                ResultSet rs = psInsertJugador.getGeneratedKeys();
                if (rs.next()) {
                    jugadorId = rs.getInt(1);
                } else {
                    throw new SQLException("No s'ha pogut obtenir l'ID del jugador.");
                }
            }


           /* PreparedStatement psJugadors = con.prepareStatement(insertTableJugadors);
            psJugadors.setString(1, username); //username
            int addRowsJugadors = psJugadors.executeUpdate();

            if (addRowsJugadors > 0) {
                System.out.println("S'han insertat les dades correctament");
            }else  System.out.println("problema jugadors");
            psJugadors.close();*/

            try {
                PreparedStatement psPartides = con.prepareStatement(insertTablePartides);


                psPartides.setInt(1, jugadorId); //id jugador
                psPartides.setInt(2, points); //punts
                psPartides.setInt(3, errors); //num errors
                psPartides.setInt(4, duration); //temps ha durat la partida
                psPartides.setTimestamp(5, new Timestamp(System.currentTimeMillis())); //hora de partida
                int addRowsPartides = psPartides.executeUpdate();

                //comprovar que les dades s'han guardat correctament si el valor de addRows>0
                if (addRowsPartides > 0) {
                    System.out.println("S'han insertat les dades correctament");
                } else {System.out.println("Hi ha hagut un problema inserint els registres a la taula partides."); }
                psPartides.close(); //potser com qeu s'obre i tanca puc posar els dos com a ps enlloc de psPartides
                con.close();


            } catch (Exception e) {
                System.out.println("No s'han pogut inserir els registres a la taula partides.");
            }

        } catch (SQLException e) {
            System.out.println("La connexió amb la base de dades ha fallat");
        }
    }
}


