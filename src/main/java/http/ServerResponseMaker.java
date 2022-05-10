package http;

public class ServerResponseMaker {
    public static String versionShaper(){
        return "HTTP/1.1";
    }
    public static String statusTextShaper(int statusCode){
        return switch (statusCode) {
            case (200) -> "OK";
            case (201) -> "Created";
            case (400) -> "Bad Request";
            case (401) -> "Unauthorized";
            case (403) -> "Forbidden";
            case (500) -> "Internal Server Error";
            default -> null;
        };
    }
    public static String contentType(String type){
        return "Content-Type: " + type;
    }

    public static String responseMaker(int statusCode, String type, String result){
        return ServerResponseMaker.versionShaper() + " " + statusCode + " " + ServerResponseMaker.statusTextShaper(statusCode) +
                '\n' + ServerResponseMaker.contentType("text/plain") + '\n' +
                '\n' + result;
    }

}
