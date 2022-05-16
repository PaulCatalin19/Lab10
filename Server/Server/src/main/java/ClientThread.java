import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Objects;

class ClientThread extends Thread {
    static ArrayList<Client> clients=new ArrayList<>();
    private Socket socket = null ;
    private static Integer TIMEOUT =3600_000;
    Server server ;
    public ClientThread (Socket socket,Server server) throws SocketException {
        this.socket = socket ;
        socket.setSoTimeout(TIMEOUT);
        this.server = server;
    }

    public void run () {
        try {
            String request ="continue";
            while(true){
                // Get the request from the input stream: client → server
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                request = in.readLine();
                // Send the response to the output stream: server → client
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String response="";

                if (Objects.equals(request, "stop")){
                    out.println("Server stopped");
                    out.flush();
                    Server.stopServerGracefully();
//                    server.setRunning(false);
                }else if(Objects.equals(request, "exit")){
                    System.out.println("Client disconnected. Stopping thread...");
                    return;
                }
                else{
                    response="Server received the request "+request;

                    ClientManagement clientManagement=new ClientManagement(request);
                    response=response+";"+clientManagement.messages;
                    out.println(response);
                }

                out.flush();
            }
        }catch (SocketTimeoutException e) {
            System.out.printf("%d seconds passed since the last request. Timeout.", TIMEOUT / 1000);
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close(); // or use try-with-resources
                System.out.println("Thread stopped");
                Server.threadCount--;
            } catch (IOException e) { System.err.println (e); }
        }
    }
}