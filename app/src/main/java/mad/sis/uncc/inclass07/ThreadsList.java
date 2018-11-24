package mad.sis.uncc.inclass07;

import java.io.Serializable;
import java.util.ArrayList;

public class ThreadsList implements Serializable{
    ArrayList<Threads> threads;

    @Override
    public String toString() {
        return "ThreadsList{" +
                "threadsListClass=" + threads +
                '}';
    }
}
