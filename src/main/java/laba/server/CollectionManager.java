package laba.server;

import collection.Human;
import commands.Command;
import laba.authorization.UserDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;


public class CollectionManager {

//    private static Map<String, Command> availableCommands = new HashMap<>();
//
//    static {
//        availableCommands.put("add", add());
//        availableCommands.put("add_if_last", addIfLast());
//        availableCommands.put("clean", clean());
//        availableCommands.put("help", help());
//        availableCommands.put("info",new Info());
//        availableCommands.put("remove",new Remove());
//        availableCommands.put("remove_last",new RemoveLast());
//        availableCommands.put("auth",new Auth());
//        availableCommands.put("show",new Show());
//        availableCommands.put("human_format",new HumanFormat());
//        availableCommands.put("sort",new Sort());
//    }
//
//    public static Command getCommand(String commandName){
//        return availableCommands.get(commandName);
//    }

//    public String getHumanName(int id) throws SQLException {
//        PreparedStatement stmt = connection.prepareStatement("SELECT name FROM collection WHERE id=?");
//        stmt.setString(1, String.valueOf(id));
//        ResultSet rs = stmt.executeQuery();
//        if (rs.next()) return rs.getString("name");
//        else return "wrong";
//    }


    public static synchronized String add(Human human, String login){
        try{
            if (human!=null) {
                PreparedStatement stmt = UserDAO.getConnection().prepareStatement("INSERT INTO collection (userLogin, name, course, birthdate, campus, floor, time) VALUES (?,?,?,?,?,?,?)");
                stmt.setString(1, login);
                stmt.setString(2, human.getName());
                stmt.setInt(3, human.getCourse());
                stmt.setString(4, human.getSimpleDate());
                stmt.setString(5, String.valueOf(human.getLocation().getCampus()));
                stmt.setInt(6, human.getLocation().getFloor());
                stmt.setString(7, String.valueOf(human.getTime()));
                stmt.execute();

            }
            return "Human is added";
        }catch (SQLException e){
            e.printStackTrace();
        }return "Nothing added in collection";
    }

    public static synchronized String addIfLast(Human human, String login){
        try {
            if (human != null) {
            Statement stmt = UserDAO.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM collection order by name limit 1");

            if (rs.next()) {
                String name = rs.getString("name");
                if (name.compareTo(human.getName())<0) {
                    add(human, login);
                    return "Human is added";
                }
            }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "nothing added in collection";
    }

    public static synchronized String help(){
        return "Команды для интерактивного управления коллекцией \n" +
                "   add:                  добавит новый элемент в бд.\n" +
                "   add_if_last:          добавит элемент в коллекцию, если имя этого объекта по алфавиту в меньшее.\n" +
                "   clean:                очистит бд от твоих элементов. \n" +
                "   human_format:         выводит информацию о задании Human в json. \n" +
                "   info:                 выводит в стандартный поток вывода основную информацию о коллекции.\n" +
                "   remove:               удалит элемент из коллекции если его создал ты.\n" +
                "   remove_if_last:       удалит из коллекции элемент если он намименьший (по алфивиту) и его добваил ты.\n" +
                "   show:                 выводит в стандартный поток вывода все элементы.\n" +
                "   exit:                 выход из программы. \n";

    }

    public static synchronized String humanFormat(){
        return "Формат задания Human в json \n" +
                "{ \n" +
                "  name':           String, \n" +
                "  birthDate':      Date [dd.MM.yyyy], \n" +
                "  course:          int [1; 6], \n" +
                "  location: { \n" +
                "               campus:  [KRONVER, LOMO, BIRJA], \n" +
                "               floor:   int [1; 4] \n" +
                "            }\n"+
                "} \n" +
                "   '- required value";
    }

    //todo
    public static synchronized String info(){

        try {
            if (!UserDAO.isEmpty()) {
                Statement stmt = UserDAO.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT count(name) FROM collection");
                if (rs.next()) {
                    return "Количество элементов в кэллекции:" + rs.getInt(1);
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "No humans added";

    }

    public static synchronized String clean(String login){
        try {
            PreparedStatement stmt = UserDAO.getConnection().prepareStatement("DELETE FROM collection * where userLogin =?");
            stmt.setString(1, login);
            stmt.execute();
            return "Collection is cleaned";
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "There is no human to remove";
    }

    public static synchronized String remove(Human human, String login){

        try {
            String inf1 = info();

            PreparedStatement stmt = UserDAO.getConnection().prepareStatement("DELETE FROM collection * where name = ? and userLogin = ?");
            stmt.setString(1, human.getName());
            stmt.setString(2, login);
            stmt.execute();
            String inf2 = info();
            if (!inf1.equals(inf2))
                return "Human is removed";
        }catch (SQLException | NullPointerException e){
            e.printStackTrace();
        }
        return "Nothing is removed";
    }

    public static synchronized String removeIfLast(Human human, String login){
        try {
            if (human != null) {
                Statement stmt = UserDAO.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name FROM collection order by name limit 1");

                if (rs.next()) {
                    String name = rs.getString("name");
                    if (name.compareTo(human.getName())<0) {
                        remove(human, login);
                        return "Human is removed";
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "There is no human to remove";

    }

    public static synchronized String show() {
        try {
            Statement stmt = UserDAO.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM collection");

            StringBuilder stringBuilder = new StringBuilder();
           
            
            stringBuilder.append("login, name, course, birthDate, campus, floor, time \n");
            while (rs.next()){
                stringBuilder.append(rs.getString("userLogin") + ", " + rs.getString("name")
                        + ", " + rs.getInt("course") + ", " + rs.getString("birthDate") + ", " + rs.getString("campus")
                        + ", " + rs.getInt("floor") + ", " + rs.getString("time") +"\n");
                
            }
            return new String(stringBuilder);

        }catch (SQLException e){
            e.printStackTrace();
        }
                return "There is no added humans";
    }




}
