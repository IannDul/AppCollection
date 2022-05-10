package net;

import collection.DragonDAO;
import com.fasterxml.jackson.core.JsonParseException;
import commands.Command;
import commands.dependencies.CommandProperties;
import commands.dependencies.Instances;
import http.ServerResponseMaker;
import io.Autosaver;
import io.FileManipulator;
import io.ServerOutput;
import json.Json;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class HttpServer {
    private static final Instances instances = new Instances();
    private final String host;
    private final int port;
    private final ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private int statusCode;
    Map<String, String> accounts;
    ArrayList<String> activeLogins;

    public HttpServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        instances.outPutter = new ServerOutput();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(host, port));
        serverSocketChannel.configureBlocking(true);
        accounts = new HashMap<>();
        activeLogins = new ArrayList<>();
    }

    public static void main(String[] args) {
    }

    private void writeLayer(List<String> list, SocketChannel socketChannel) throws IOException {
        StringBuilder result = new StringBuilder(
                list.stream()
                        .mapToInt(String::length)
                        .sum()
        );
        String response = "";

        for (String msg : list) {
            result.append(msg);
        }
        response = ServerResponseMaker.responseMaker(statusCode, "text/plain", result.toString());
        System.out.println(response);
        try {
            write(response, socketChannel);
        } catch (NullPointerException e) {
            instances.outPutter.output(e.getMessage());
        }
    }

    private void write(String message, SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(20008);
        buffer.clear();
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
        System.out.println(StandardCharsets.UTF_8.decode(buffer));
    }

    private String parseInput(String input) {
        char lineSep = '\n';
        boolean sumString = false;
        StringBuilder inputString = new StringBuilder();

        for (int i = 1; i < input.length(); i++) {
            if (sumString) {
                inputString.append(input.charAt(i));
            }
            if (input.charAt(i) == lineSep && input.charAt(i - 1) == lineSep) {
                sumString = true;
            }
        }
        return inputString.toString();
    }

    private String read(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(20008);
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        String input = StandardCharsets.UTF_8.decode(buffer).toString();

        return parseInput(input);
    }

    public void run() throws IOException, NullPointerException {

        try {
            instances.dao = FileManipulator.get();
        } catch (RuntimeException e) {
            instances.outPutter.output(e.getMessage());
            instances.dao = new DragonDAO();
        }


        while (true) {
            socketChannel = serverSocketChannel.accept();
            String input = read(socketChannel);
            statusCode = 200;
            try {
                Command command = Command.restoreFromProperties(Json.fromJson(Json.parse(input), CommandProperties.class));
                command.execute(instances);
                statusCode = command.getStatusCode();


            } catch (JsonParseException e) {
                instances.outPutter.output("Ben запретил такое отправлять" + System.lineSeparator() + e.getMessage());
                statusCode = 400;

            } catch (IOException e) {
                instances.outPutter.output("Ben запретил такое отправлять" + System.lineSeparator() + e.getMessage());
                statusCode = 400;
            }

            List<String> list = instances.outPutter.compound();

            writeLayer(list, socketChannel);


            Autosaver.autosave(instances);

            list.clear();

//            if (input.equals("Send me logins")) {
//                instances.outPutter.output(getLogins());
//                List<String> list = instances.outPutter.compound();
//                writeLayer(list, socketChannel);
//                list.clear();
//
//            }
//            else if (input.charAt(0) == 72){
//                checkPassword(parseLoginAndPassword(input));
//            }
//            else if (input.charAt(0) == 78){
//                newAccount(parseLoginAndPassword(input));
//            }
//            else {
//
//                try {
//                    String[] infoAndLogin = input.split(String.valueOf('\t'));
//                    if (activeLogins.contains(infoAndLogin[1])){
//                        Command command = Command.restoreFromProperties(Json.fromJson(Json.parse(infoAndLogin[0]), CommandProperties.class));
//                        command.execute(instances);
//                        statusCode = command.getStatusCode();
//                    }
//                    else{
//                        instances.outPutter.output("Вы не зарегистрированы, переподключитесь");
//                        statusCode = 400;
//                    }
//
//
//                } catch (JsonParseException e) {
//                    instances.outPutter.output("Ben запретил такое отправлять" + System.lineSeparator() + e.getMessage());
//                    statusCode = 400;
//
//                } catch (IOException e) {
//                    instances.outPutter.output("Ben запретил такое отправлять" + System.lineSeparator() + e.getMessage());
//                    statusCode = 400;
//                }
//
//                List<String> list = instances.outPutter.compound();
//
//                writeLayer(list, socketChannel);
//
//
//                Autosaver.autosave(instances);
//
//                list.clear();
//            }

        }

    }
}

//    private String[] parseLoginAndPassword(String loginAndPassword){
//        return loginAndPassword.split(String.valueOf('\t'));
//    }
//
//    private void newAccount(String[] loginAndPassword) {
//        accounts.put(loginAndPassword[1], loginAndPassword[2]);
//        instances.outPutter.output("Новый пользователь создан" + System.lineSeparator());
//        List<String> list = instances.outPutter.compound();
//        try {
//            writeLayer(list, socketChannel);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        list.clear();
//    }
//
//    private void checkPassword(String[] loginAndPassword) {
//        if (accounts.get(loginAndPassword[1]).equals(loginAndPassword[2])){
//            instances.outPutter.output("YES");
//            activeLogins.add(loginAndPassword[1]);
//            List<String> list = instances.outPutter.compound();
//            try {
//                writeLayer(list, socketChannel);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            list.clear();
//        }
//        else{
//            instances.outPutter.output("NO");
//            List<String> list = instances.outPutter.compound();
//            try {
//                writeLayer(list, socketChannel);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            list.clear();
//        }
//    }
//
//    private String getLogins(){
//        Set<String> setKeys = accounts.keySet();
//        StringBuilder logins = new StringBuilder();
//        for(String k : setKeys){
//            logins.append(k).append('\t');
//        }
//        return String.valueOf(logins);
//    }
