package Server;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseService {
    private final String AUTH_DB_PATH = "./src/Server/auth.txt";
    private final String MESSAGES_DB_PATH = "./src/Server/messages.txt";


    public DatabaseService() {
    }

    public String getUserPassword(String targetedUsername) {
        try {
            FileReader fileReader = new FileReader(AUTH_DB_PATH);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitLine = line.split(",");
                String username = splitLine[0];
                String password = splitLine[1];

                if (username.equals(targetedUsername)) {
                    return password;
                }
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return null;
        }
        return null;
    }
    public void addUser(String username, String password) {
        try {
            FileWriter fileWriter = new FileWriter(AUTH_DB_PATH, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(username + "," + password);
            bufferedWriter.newLine();

            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    public void addMessage(Message message) {
        try {
            FileWriter fileWriter = new FileWriter(MESSAGES_DB_PATH, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(
            encodeCommas(message.getMessage()) + "," +
                message.getUsername() + "," +
                message.getIpAddress() + "," +
                message.getPort() + "," +
                Message.dateFormatter.format(message.getDate()) + "," +
                Message.timeFormatter.format(message.getTime()) + ","
            );
            bufferedWriter.newLine();

            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    public ArrayList<Message> getLastMessages(int linesToRead) {
        File file = new File(MESSAGES_DB_PATH);
        ArrayList<Message> messages = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            long fileLength = file.length();
            if(fileLength == 0) {
                return messages;
            }

            long position = fileLength - 1;
            int linesRead = 0;
            StringBuilder lastLine = new StringBuilder();

            randomAccessFile.seek(position);

            while (position > 0 && linesRead < linesToRead) {
                position--;
                randomAccessFile.seek(position);
                char c = (char) randomAccessFile.read();
                if (c == '\n') {
                    linesRead++;
                    messages.add(processMessageLine(lastLine.toString()));
                    lastLine = new StringBuilder();
                }
                lastLine.insert(0, c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(messages);
        return messages;
    }

    private static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    private Message processMessageLine(String line) {
        String[] splitLine = line.split(",");
        return new Message(
                decodeCommas(splitLine[0]),
                splitLine[1],
                splitLine[2],
                Integer.parseInt(splitLine[3]),
                LocalDate.parse(splitLine[4], Message.dateFormatter),
                LocalTime.parse(splitLine[5], Message.timeFormatter)
        );
    }

    private String encodeCommas(String message) {
        return message.replaceAll(",", "f73ks73n");
    }

    private String decodeCommas(String message) {
        return message.replaceAll("f73ks73n", ",");
    }
}
