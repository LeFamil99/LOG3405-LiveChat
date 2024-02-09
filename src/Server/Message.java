package Server;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final String message;
    private final String username;
    private final String ipAddress;
    private final int port;
    private final LocalDate date;
    private final LocalTime time;

    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public Message(String message, String username, String ipAddress, int port, LocalDate date, LocalTime time) {
        this.message = message;
        this.username = username;
        this.ipAddress = ipAddress;
        this.port = port;
        this.date = date;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

}
