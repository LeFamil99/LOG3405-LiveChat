package Server;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseService {
    private final String AUTH_DB_PATH = "./auth.txt";
    private final String MESSAGES_DB_PATH = "./messages.txt";


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
            message.getMessage() + "," +
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

    public ArrayList<Message> getLastMessages(int amount) {
        try {
            ArrayList<Message> messages = new ArrayList<>();

            RandomAccessFile randomAccessFile = new RandomAccessFile(MESSAGES_DB_PATH, "r");

            FileChannel fileChannel = randomAccessFile.getChannel();

            long fileLength = fileChannel.size();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            for (long position = fileLength - 1; position >= 0 && messages.size() <= amount; position--) {
                fileChannel.position(position);

                int bytesRead = fileChannel.read(buffer);

                for (int i = bytesRead - 1; i >= 0; i--) {
                    char currentChar = (char) buffer.get(i);
                    if (currentChar == '\n') {
                        messages.add(processMessageLine(buffer, i, bytesRead));
                    }
                }
                buffer.rewind();
            }

            fileChannel.close();
            randomAccessFile.close();

            return messages;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private Message processMessageLine(ByteBuffer buffer, int start, int end) {
        byte[] lineBytes = new byte[end - start];
        buffer.get(lineBytes);

        String line = new String(lineBytes);
        String[] splitLine = line.split(",");

        return new Message(
                splitLine[0],
                splitLine[1],
                splitLine[2],
                Integer.parseInt(splitLine[3]),
                LocalDate.parse(splitLine[4], Message.dateFormatter),
                LocalTime.parse(splitLine[5], Message.timeFormatter)
        );
    }
}
