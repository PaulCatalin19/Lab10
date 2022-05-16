import javax.swing.plaf.IconUIResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClientManagement {

    static Client currentClient;
    static boolean loggedin = false;
    String messages="";

    public ClientManagement(String command) {
        String[] splittedCommand = command.split(" ");

        if (command.contains("register")){
            register(splittedCommand[1]);
        } else if(command.contains("login")){
            System.out.println(splittedCommand[1]);
            login(splittedCommand[1]);
        } else if (command.contains("friends") ||command.contains("friend")){
            friend(Arrays.copyOfRange(splittedCommand,1,splittedCommand.length));
        }else if (command.contains("send")){
            splittedCommand=command.split(" ",2);
            send(splittedCommand[1]);
        }else if (command.contains("read")){
            read();
        }
    }

    private void register(String name){

        for (Client client: ClientThread.clients) {
            if(Objects.equals(client.getName(), name))
            {
//                System.out.println("You are registered already");
                messages="You are registered already";
                return;
            }
        }
        currentClient=new Client(name);
        ClientThread.clients.add(currentClient);
//        System.out.println("Register succeeded");
        messages="Register succeeded";
        loggedin=false;
    }

    private void login (String name){
        for (Client client: ClientThread.clients) {
//            System.out.println("Clients: "+client.getName());
            if(Objects.equals(client.getName(), name)){
//                System.out.println("Log in succeeded");
                messages="Log in succeeded";
                currentClient=client;
                loggedin= true;
                return;
            }
        }
//        System.out.println("You can't log in, you need to register firstly");
        messages="You can't log in, you need to register firstly";
        loggedin= false;
    }

    private void friend (String[] names){
        if(loggedin==true){
            for (Client client: ClientThread.clients){
                for(String name:names ){
                    if(Objects.equals(client.getName(), name) && !Objects.equals(currentClient.getName(), name)){
                        currentClient.setFriends(client);
                        client.setFriends(currentClient);
                        messages=name+" is your friend now";
//                        System.out.println(name+" is "+currentClient.getName()+"'s friend new");
                    }
                }
            }
            System.out.println(currentClient.getName()+"'s friends are: ");
            for (Client c:currentClient.getFriends()) {
                System.out.println(" -> "+c.getName()+" ");
            }
        }else {
//            System.out.println("You need to log in to add friends ");
            messages="You need to log in to add friends ";
        }
    }

    private void send (String str){
        if(loggedin==true){
            if(currentClient.getFriends().size()>0){
            for (Client c:currentClient.getFriends()) {
                c.setMessage(currentClient.getName()+" sent message "+str);
//                System.out.println(currentClient.getName()+" sent message "+str+" to "+c.getName());
                messages=messages+"You sent message "+str+" to "+c.getName()+";";
            }
            }
            else {
                messages="You need to add some friends to your network ";
//                System.out.println("You need to add some friends to your network ");
            }
        }else {
            messages="You need to log in to send messages ";
//            System.out.println("You need to log in to send messages ");
        }
    }

    private void read (){
        if(loggedin==true){
            if(currentClient.getMessage().size()>0){
            for (String str:currentClient.getMessage()) {
                System.out.println(str+" to "+currentClient.getName());
                messages=messages+str+";";
            }

            }else {
                messages="You don't have any messages";
//                System.out.println("You don't have any messages");
            }

        }else {
            messages="You need to log in to send messages ";
//            System.out.println("You need to log in to send messages ");
        }
    }

}
