package laba.authorization;

import laba.mail.MailSender;

import java.sql.*;

public class DB {
    private static final Connection connection;

    static {
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "зщыепкуы");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection = c;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String getUserPassword(String username) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM studs WHERE user_login='" + username + "'");

        if (rs.next()){
            return rs.getString("password");
        } else {
            return "wrong pwd";
        }
    }

    public String getUserMail(String username) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM studs WHERE user_login=?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getString("user_mail");
        else return "wrong mail";
    }

    public static boolean isLoginAvailable(String username) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM studs WHERE user_login=?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return !rs.next();
    }


    public static boolean addUser(String login, String mail){
        try{
            String pwd = Password.generateString(10);
            if (MailSender.sendPassword(pwd, mail) && !login.equals("") && isLoginAvailable(login) ){

                String password= Password.generatePassword(pwd, "salt");
                PreparedStatement stmt = DB.getConnection().prepareStatement("INSERT INTO studs (user_login, password, user_mail) VALUES (?,?,?)");
                stmt.setString(1, login);
                stmt.setString(2, password);
                stmt.setString(3, mail);
                stmt.execute();
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }return false;
    }

    public static boolean isEmpty(){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT count(name) as count FROM collection");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int i = resultSet.getInt(1);
            if (i==0){
                return true;
            } else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных.");
            return false;
        }
    }



}
