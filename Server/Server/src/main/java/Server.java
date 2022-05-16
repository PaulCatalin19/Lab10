import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT =8100;
    static private boolean running = true;
    static public volatile int threadCount =0;

    private static ServerSocket serverSocket=null;
    static private boolean shouldShutDown = false;

    public Server() throws IOException {
        try{
            serverSocket= new ServerSocket(PORT);
            while(running ==true){
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                // Execute the client's request in a new thread
                new ClientThread(socket, this).start();
                threadCount++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            serverSocket.close();
        }
        System.out.println("Number of threads: " + threadCount);
        while (threadCount > 0) {
            Thread.onSpinWait();
        }
        System.out.println("Number of threads: " + threadCount);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    static public void stopServerGracefully() {
        System.out.println("Server initiated by a client gracefully");
        shouldShutDown = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException{
        Server server = new Server();
    }
}
