package http;

public class ClientRequestMaker {

    public static String MethodShaper(String commandName){
        return switch (commandName) {
            case ("add"), ("add_if_max"), ("createAccount") -> "POST";
            case ("help"), ("info"), ("history"), ("min_by_id"),
                    ("count_by_age"), ("filter_greater_than_character"), ("show"), ("check") -> "GET";
            case ("update"), ("sort") -> "PUT";
            case ("remove_by_id"), ("clear") -> "DELETE";
            default -> null;
        };
    }

//    public static String pathShaper(String URL){
//        //I know nothing about this
//        return URL;
//    }

    public static String versionShaper(){
        return "HTTP/1.1";
    }

    public static String hostShaper(String host, int port){
        return "HOST: " + host + ":" + port;
    }

    public static String contentType(String type){
        return "Content-Type: " + type;
    }

    public static String acceptingData(String typeOfData){
        return "Accept: " + typeOfData;
    }

    public static String bodyShaper(String request){
        return request;
    }

    public static String requestShaper(String commandName, String host, int port, String type, String request){

        return ClientRequestMaker.MethodShaper(commandName) + " " + ClientRequestMaker.pathShaper(commandName) + " " +
                ClientRequestMaker.versionShaper() + '\n' + ClientRequestMaker.hostShaper(host, port) +
                '\n' + ClientRequestMaker.contentType(type) + '\n' +
                '\n' + ClientRequestMaker.bodyShaper(request);
    }

    public static String pathShaper(String commandName){
        return switch (commandName) {
            case ("add"), ("add_if_max"), ("clear"), ("help"), ("info"), ("history"), ("show") -> "/dragons";
            case ("min_by_id"), ("update"), ("remove_by_id") -> "/dragons/{dragonId}";
            case ("sort"), ("filter_greater_than_character") ->  ("/dragons/{dragonId}/dragonCharacter");
            case ("count_by_age") -> "/dragons/{dragonId}/dragonAge";
            case ("check"), ("createAccount") -> "/login/password";
            default -> null;
        };
    }



}
