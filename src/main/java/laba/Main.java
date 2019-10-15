package laba;

import laba.server.CollectionManager;
import laba.server.RequestHandler;
import laba.server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        int port = 3080;
        RequestHandler handler = new RequestHandler();

        System.out.println("Starting server ...");
        try{
            new Thread(new Server(new ServerSocket(port)).setHandler(handler)).start();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }



    }


}

