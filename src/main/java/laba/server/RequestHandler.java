package laba.server;

import commands.Command;
import laba.authorization.Auth;
import laba.authorization.Security;

public class RequestHandler {


    public String doCommand(Command request){

        if (request!=null) {
            String commandName = request.getCommandName();

            String login = request.getLogin();
            String password = request.getPassword();


            if (commandName.equals("register")){
                return Auth.register(request.getLogin(), request.getMail());
            }else if (commandName.equals("login")){
                return Auth.login(request.getLogin(), request.getPassword());
            }
            while(Security.checkLog(login,password).equals("ok")){
                switch (commandName){
                    case "add":
                        return CollectionManager.add(request.getHuman(), login);
                    case "add_if_last":
                        return CollectionManager.addIfLast(request.getHuman(), login);
                    case "remove":
                        return CollectionManager.remove(request.getHuman(), login);
                    case "remove_if_last":
                        return   CollectionManager.removeIfLast(request.getHuman(),login);
                    case "human_format":
                        return   CollectionManager.humanFormat();
                    case "help":
                        return   CollectionManager.help();
                    case "info":
                        return  CollectionManager.info();
                    case "show":
                        return   CollectionManager.show();
                    case "clean":
                        return   CollectionManager.clean(login);
                    default:
                        return  "Команда с именем "+commandName+" не найдена";

                }
            }return "Login or register if you don't have an account";

        }return "null !";

    }
}
