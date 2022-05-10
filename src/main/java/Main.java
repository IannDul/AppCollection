import net.HttpServer;
import net.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws NullPointerException, IOException {
        try{
            HttpServer server = new HttpServer("localhost", 80);
            server.run();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

class Client1{
    public static void main(String[] args) throws InterruptedException {
        try {
            HttpClientLayer layer = new HttpClientLayer("localhost", 80);
            layer.run();
        } catch (IOException e) {
            System.out.println("не удалось создать клиент");
        }

    }
}


class Client2{
    public static void main(String[] args) throws InterruptedException {
        try {
            HttpClientLayer layer2 = new HttpClientLayer("localhost", 80);
            layer2.run();
        } catch (IOException e) {
            System.out.println("не удалось создать клиент");
        }

    }
}

class UDPServer{
    public static void main(String[] args) throws NullPointerException, IOException {
        try{
            Server server = new Server("localhost", 80);
            server.run();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

class UDPClient{
    public static void main(String[] args) throws InterruptedException {
        try {
            ClientLayer layer2 = new ClientLayer();
            layer2.run();
        } catch (IOException e) {
            System.out.println("не удалось создать клиент");
        }

    }
}

