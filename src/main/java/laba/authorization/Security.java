package laba.authorization;

import java.sql.SQLException;

public class Security {

    public static String checkLog(String login, String password){
        try {
            if(login!=null && password!=null && DB.getUserPassword(login).equals(Password.generatePassword(password, "salt")))
                return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "You have to login or register if you don't have an account";
    }
}
