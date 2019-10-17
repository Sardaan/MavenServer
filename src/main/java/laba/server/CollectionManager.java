package laba.server;

import collection.Human;
import laba.authorization.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CollectionManager {

    static ConcurrentLinkedDeque<Human> humans = new ConcurrentLinkedDeque<>();

    public static synchronized String add(Human human, String login){
        try{
            if (human!=null) {
                humans.add(human);
                PreparedStatement stmt = DB.getConnection().prepareStatement("INSERT INTO collection (userLogin, name, course, birthdate, campus, floor, time) VALUES (?,?,?,?,?,?,?)");
                stmt.setString(1, login);
                stmt.setString(2, human.getName());
                stmt.setInt(3, human.getCourse());
                stmt.setString(4, human.getSimpleDate());
                stmt.setString(5, String.valueOf(human.getLocation().getCampus()));
                stmt.setInt(6, human.getLocation().getFloor());
                stmt.setString(7, String.valueOf(human.getTime()));
                stmt.execute();
                return "Human is added";
            }

        }catch (SQLException e){
            e.printStackTrace();
        }return "Nothing added in collection";
    }

    public static synchronized String addIfLast(Human human, String login){
        try {
            if (human != null) {
            Statement stmt = DB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM collection order by name limit 1");

            if (rs.next()) {
                String name = rs.getString("name");
                if (name.compareTo(human.getName())<0) {
                    humans.add(human);
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
                "   logout:               выход из аккаунта. \n" +
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
            if (!DB.isEmpty()) {
                Statement stmt = DB.getConnection().createStatement();
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
            PreparedStatement stmt = DB.getConnection().prepareStatement("DELETE FROM collection * where userLogin =?");
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
            humans.remove(human);
            PreparedStatement stmt = DB.getConnection().prepareStatement("DELETE FROM collection * where name = ? and userLogin = ?");
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
                Statement stmt = DB.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name FROM collection order by name limit 1");

                if (rs.next()) {
                    String name = rs.getString("name");
                    if (name.compareTo(human.getName())<0) {
                        humans.remove(human);
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
            if (!DB.isEmpty()) {
                Statement stmt = DB.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM collection");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("login, name, course, birthDate, campus, floor, time \n");
                while (rs.next()) {
                    stringBuilder.append(rs.getString("userLogin") + ", " + rs.getString("name")
                            + ", " + rs.getInt("course") + ", " + rs.getString("birthDate") + ", " + rs.getString("campus")
                            + ", " + rs.getInt("floor") + ", " + rs.getString("time") + "\n");

                }
                return new String(stringBuilder);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return "There is no added humans";
    }




}
