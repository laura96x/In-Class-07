package mad.sis.uncc.inclass07;

import java.io.Serializable;
import java.util.Arrays;

public class Threads implements Serializable{

    String user_fname, user_lname, user_id, id, title, created_at;

    public Threads() {
    }

    @Override
    public String toString() {
        return "Threads{" +
                "user_fname='" + user_fname + '\'' +
                ", user_lname='" + user_lname + '\'' +
                ", user_id='" + user_id + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
