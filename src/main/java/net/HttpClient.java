package net;

import commands.dependencies.Instances;
import io.ConsoleOutput;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpClient {

    static Instances instances = new Instances();
    String host;
    int port;

    int MAX_PACKET_LENGTH = 10008;

    public HttpClient(String host, int port){
        instances.outPutter = new ConsoleOutput();
        this.host = host;
        this.port = port;

    }



    private void write(SocketChannel socketChannel, String message) throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(20008);
        buffer.clear();
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
    }

    private String read(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_LENGTH * 2); // UTF 16 encoding
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        String input = StandardCharsets.UTF_8.decode(buffer).toString();

        return parseInput(input);
    }

    private String attachInput(List<String> input) {
        StringBuilder b = new StringBuilder();
        input.forEach(b::append);
        return b.toString();
    }

    public String sendAndReceiveResponse(String message) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, port));
        write(socketChannel, message);
        return read(socketChannel);
    }

    private String parseInput(String input){
        char lineSep = '\n';
        boolean sumString = false;
        StringBuilder inputString = new StringBuilder();

        for(int i = 1; i < input.length(); i++){
            if (sumString){
                inputString.append(input.charAt(i));
            }
            if (input.charAt(i) == lineSep && input.charAt(i - 1) == lineSep){
                sumString = true;
            }
        }
        return inputString.toString();
    }

//    public static String toMD5(String st) {
//        MessageDigest messageDigest = null;
//        byte[] digest = new byte[0];
//
//        try {
//            messageDigest = MessageDigest.getInstance("MD5");
//            messageDigest.reset();
//            messageDigest.update(st.getBytes());
//            digest = messageDigest.digest();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//        BigInteger bigInt = new BigInteger(1, digest);
//        String md5Hex = bigInt.toString(16);
//
//        while( md5Hex.length() < 32 ){
//            md5Hex = "0" + md5Hex;
//        }
//
//        return md5Hex;
//    }

}