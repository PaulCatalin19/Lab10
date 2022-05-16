import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    private String name;
    private ArrayList<Client> friends= new ArrayList<>();
    private ArrayList<String> message= new ArrayList<>();

    public Client(String name) {
        this.name = name;
//        System.out.println(name);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Client> getFriends() {
        return friends;
    }

    public void setFriends(Client friend) {
        this.friends.add(friend);
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(String m) {
        this.message.add(m);
    }
}
