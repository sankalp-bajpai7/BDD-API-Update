package Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class testObj {

    String id= "";
    String username="";

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentTime() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH/mm/ssS");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);
        // System.out.println()
        return currentTime;
    }
}
