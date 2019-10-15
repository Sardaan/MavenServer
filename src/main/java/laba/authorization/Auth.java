package laba.authorization;

import java.sql.SQLException;


public class Auth {
    
    public static String login(String login, String password){
        try {
            if (UserDAO.getUserPassword(login).equals(Password.generatePassword(password,"salt"))){

                return "You are successfully login";

            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return "¯\\_(ツ)_/¯ wrong password or login";

    }

    public static String register(String login, String mail){
        try {
            if (UserDAO.isLoginAvailable(login)){
                new UserDAO().addUser(login, mail);
                return "You are successfully registered";
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "You are not registered!";
    }

}
