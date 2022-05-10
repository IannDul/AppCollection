import com.fasterxml.jackson.core.JsonProcessingException;
import commands.Command;
import commands.CommandCreator;
import commands.ExecuteScript;
import commands.dependencies.CommandProperties;
import commands.dependencies.Instances;
import exceptions.InvalidArgsSizeException;
import exceptions.InvalidValueException;
import exceptions.ProgramExitException;
import http.ClientRequestMaker;
import io.ConsoleOutput;
import json.Json;
import net.HttpClient;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.*;

public class HttpClientLayer {
    private final HttpClient client;
    private final Instances instances = new Instances();
    String host;
    int port;
    Scanner scanner;

    public HttpClientLayer(String host, int port) throws IOException {
        client = new HttpClient(host, port);
        this.host = host;
        this.port = port;
        instances.outPutter = new ConsoleOutput();
        scanner = new Scanner(System.in);
    }

    private List<CommandProperties> handleExecuteScript(ExecuteScript script) throws InvalidValueException {
        List<CommandProperties> properties = new ArrayList<>();

        for(Command c: script.getNestedCommands(instances))
            properties.add(c.getProperties(instances));

        return properties;
    }

    private String serialize(CommandProperties p) throws JsonProcessingException {
        return Json.stringRepresentation(Json.toJson(p), false);
    }

    public void loop() throws IOException, InterruptedException {
        List<Command> commands;
        try {
            commands = CommandCreator.getCommands(instances.consoleReader);
        } catch (InvalidArgsSizeException e) {
            instances.outPutter.output(e.getMessage());
            return;
        } catch (NoSuchElementException e) {
            throw new ProgramExitException("Завершение программы...");
        } catch (NullPointerException e) {
            instances.outPutter.output("Такой команды не существует. Введите help для подробной информации");
            return;
        }

        List<CommandProperties> commandProperties = new ArrayList<>();

        for(Command c: commands) {
            if (c instanceof ExecuteScript)
                Instances.filePathChain.clear();
            try {
                if (c instanceof ExecuteScript)
                    commandProperties.addAll(handleExecuteScript((ExecuteScript) c));
                else
                    commandProperties.add(c.getProperties(instances));
            } catch (NullPointerException e) {
                instances.outPutter.output("Одна или несколько команд не были распознаны");
                return;
            } catch (RuntimeException e) {
                instances.outPutter.output(e.getMessage());
                return;
            } catch (InvalidValueException e) {
                e.printStackTrace();
            }
        }

        for (CommandProperties p: commandProperties) {
            String request;
            String bigRequest;

            // Закончить работу клиента, не отправлять команду на сервер
            if (p.args.get(0).equals("exit"))
                throw new ProgramExitException("Завершение программы...");

            try {
                request = serialize(p);
            } catch (JsonProcessingException e) {
                instances.outPutter.output("Ошибка сериализации json");
                return;
            }

            bigRequest = ClientRequestMaker.requestShaper(p.args.get(0), host, port, "application/json", request);

            String response = client.sendAndReceiveResponse(bigRequest);
            System.out.println(bigRequest + System.lineSeparator());
            instances.outPutter.output(response);
        }

    }

    public void run() throws InterruptedException, IOException {

        //String login = authorization();
        instances.outPutter.output("Введите команду. Для полного списка команд введите help");
        for(;;) {
            try {
                loop();
            } catch (ProgramExitException e) {
                instances.outPutter.output(e.getMessage());
                break;
            } catch (PortUnreachableException e) {
                instances.outPutter.output("Сервер не работает в данный момент. Повторите попытку позже");
            }
            catch (IOException e) {
                instances.outPutter.output(e.getMessage());
            }
        }
    }

//    private String alreadyHaveAccount(ArrayList<String> logins) throws IOException, InterruptedException {
//        String login = "";
//        String password = "";
//        instances.outPutter.output("Введите логин:   ");
//        while (true){
//            login = scanner.nextLine().trim();
//            if (login.length() < 5){
//                instances.outPutter.output("Логин должен состоять не менее, чем из 5 символов");
//            }
//
//            if (logins.contains(login)) {
//                    instances.outPutter.output("Введите пароль:   ");
//                    break;
//            }
//            instances.outPutter.output("Такого логина не существует! Попробуйте снова!)");
//        }
//        while (true){
//            password = scanner.nextLine().trim();
//            if(password.length() >= 5){
//
//                String smallRequest = "H" + '\t' + login + '\t' + HttpClient.toMD5(password);
//                String responseAboutPassword = client.sendAndReceiveResponse(ClientRequestMaker.requestShaper("check", host, port,
//                        "text/plain", smallRequest));
//                if (Objects.equals(responseAboutPassword, "YES")){
//                    instances.outPutter.output("Пароль введён успешно!" + System.lineSeparator());
//                    break;
//                }
//                else{
//                    instances.outPutter.output("Пароль введён неверно! Попробуйте снова!");
//                }
//            }
//            else{
//                instances.outPutter.output("Пароль должен состоять не менее чем из 5 символов! Попробуйте снова!)");
//            }
//        }
//        return login;
//    }
//
//    private void newAccount(ArrayList<String> logins) throws IOException, InterruptedException {
//        instances.outPutter.output("Придумайте логин. Логин должен состоять не менее, чем из 5 символов");
//        String login = "";
//        while(true){
//            login = scanner.nextLine().trim();
//            if (login.length() < 5){
//                instances.outPutter.output("Логин должен состоять не менее, чем из 5 символов");
//            }
//            else if(logins.contains(login)){
//                instances.outPutter.output("Такой логин уже существует! Попробуйте снова!)");
//            }
//            else{
//                instances.outPutter.output("Успешно! Теперь придумайте пароль. Пароль должен состоять не менее, чем из 5 символов");
//                logins.add(login);
//                break;
//            }
//        }
//        while(true){
//            String password = scanner.nextLine().trim();
//            if (password.length() < 5){
//                instances.outPutter.output("Пароль должен состоять не менее, чем из 5 символов");
//            }
//            else if (!unreliablePassword(password)){
//                instances.outPutter.output("Пароль ненадёжный. Попробуйте снова!)");
//            }
//            else{
//                String smallRequest = "N" + '\t' + login + '\t' + HttpClient.toMD5(password);
//                String answer = client.sendAndReceiveResponse(ClientRequestMaker.requestShaper("createAccount", host,
//                        port, "text/plain", smallRequest));
//                instances.outPutter.output(answer);
//                break;
//            }
//        }
//    }
//
//    private boolean unreliablePassword(String password){
//        int count = 0;
//        boolean reliable = true;
//        for(int i = 1; i < password.length(); i++){
//            if (password.charAt(i) == password.charAt(i - 1)){
//                count++;
//            }
//        }
//        if (count == password.length() - 1){
//            reliable = false;
//        }
//        return reliable;
//    }
//
//    private String authorization() throws IOException, InterruptedException {
//        ArrayList<String> logins = getLogins();
//        String login = "";
//        instances.outPutter.output("Привет" + System.lineSeparator() +
//                "Введите NEW , если Вы хотите создать новый аккаунт" + System.lineSeparator() +
//                "Введите ACCOUNT , если Вы хотите зайти в уже существующий аккаунт");
//
//        while(true){
//            String answer1 = scanner.nextLine().trim();
//            if (answer1.equalsIgnoreCase("NEW")){
//                newAccount(logins);
//                login = alreadyHaveAccount(logins);
//                break;
//            }
//            else if (answer1.equalsIgnoreCase("ACCOUNT")){
//                login = alreadyHaveAccount(logins);
//                break;
//            }
//            instances.outPutter.output("Попробуйте снова!");
//        }
//        return login;
//    }
//    private ArrayList<String> getLogins() {
//        String response = "";
//        try {
//            response = client.sendAndReceiveResponse("Send me logins");
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        String[] logins = response.split(String.valueOf('\t'));
//        return new ArrayList<>(Arrays.asList(logins));
//    }
}
