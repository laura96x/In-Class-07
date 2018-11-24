package mad.sis.uncc.inclass07;

public class Messages {
    String user_fname, user_lname, user_id, id, message, created_at;

    @Override
    public String toString() {
        return "Messages{" +
                "user_fname='" + user_fname + '\'' +
                ", user_lname='" + user_lname + '\'' +
                ", user_id='" + user_id + '\'' +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
