package laba.authorization;

import java.sql.SQLException;

public class Security {

    public static String checkLog(String log, String password){
        try {
            if(log!=null && password!=null && UserDAO.getUserPassword(log).equals(Password.generatePassword(password, "salt")))
                return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "You have to login or register if you don't have an account";
    }
}
