package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Main {
    public static void main(String[] args) {

        String dataBaseURL = "jdbc:mysql://localhost:3306/gamememory"; //posar nom bdd
        String user = "julia"; //canviar user
        String password = "julia"; //canviar psw

        //primer cal crear taula
        //indicar taula. cada 'interrogacio' es una columna definida a ps.set
        String insertTablePartides = "insert into partides values (?, ?, ?, ?, ?)";
        String insertTableJugadors = "insert into jugadors values (?, ?)";

        try {
            Connection con = DriverManager.getConnection(dataBaseURL, user, password);

            PreparedStatement psJugadors = con.prepareStatement(insertTableJugadors);
            psJugadors.setInt(1, 1); //id
            psJugadors.setString(2, " "); //username
            int addRowsJugadors = psJugadors.executeUpdate();

            if(addRowsJugadors > 0) {
                System.out.println("S'han insertat les dades correctament");
            }
            psJugadors.close();

            PreparedStatement psPartides = con.prepareStatement(insertTablePartides);
            psPartides.setInt(1, 1);//id
            psPartides.setInt(2, 1); //id jugador
            psPartides.setInt(3, 1); //punts
            psPartides.setInt(4, 1); //num errors
            psPartides.setInt(5, 1); //temps ha durat la partida
            psPartides.setString(5, "timestamp"); //hora de partida
            int addRowsPartides = psPartides.executeUpdate();

            //comprovar que les dades s'han guardat correctament si el valor de addRows>0
            if(addRowsPartides > 0) {
                System.out.println("S'han insertat les dades correctament");
            }
            psPartides.close(); //potser com qeu s'obre i tanca puc posar els dos com a ps enlloc de psPartides
            con.close();

        } catch (Exception e) {
            System.out.println("La connexió amb la base de dades ha fallat");
        }
    }
}
