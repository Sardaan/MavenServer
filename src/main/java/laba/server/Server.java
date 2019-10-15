package laba.server;

import commands.Command;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {

    private final ServerSocket socket;
    private RequestHandler handler;

    public Server(final ServerSocket serverSocket){
        socket = serverSocket;
    }

    public Server setHandler(RequestHandler handler){
        if (handler!=null)
            this.handler = handler;
        return this;
    }

    private synchronized void listen(final RequestHandler handler){
        try{
            Socket clientDialog = socket.accept();

            new Thread(() -> {
               try(ObjectInputStream br = new ObjectInputStream(clientDialog.getInputStream());
               BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(clientDialog.getOutputStream()))){

                   Command request = (Command) br.readObject();

                   String commandMsg = handler.doCommand(request);

                   writeData(bw, commandMsg == null ? "-" : commandMsg);
               }catch (IOException e ){
                   System.out.println(e.getMessage());
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               } finally {
                   try {
                       clientDialog.close();
                   }catch (IOException e){
                       System.out.println(e.getMessage());
                   }
               }
            }).start();


        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    private void writeData(BufferedWriter bw, String data) throws IOException {
        bw.write(data);
        bw.flush();
    }

    @Override
    public void run() {
        while(true){
            this.listen(handler);
        }
    }
}
