package mad.sis.uncc.inclass07;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MessageThreadsActivity extends AppCompatActivity implements InterfaceFunctions {

    ImageButton logout, btnAdd;
    TextView name, newThreadTitle;

    public RecyclerView recyclerView1;
    public RecyclerView.Adapter adapter1;
    public RecyclerView.LayoutManager layoutManager1;

    User user;
    ThreadsList threadsListClass;

    public static String threadKey = "thread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_threads);
        this.setTitle(R.string.msg_title);

        logout = findViewById(R.id.logoutButton);
        name = findViewById(R.id.nameView);
        newThreadTitle = findViewById(R.id.newthreadText);
        btnAdd = findViewById(R.id.buttonAdd);

        if (getIntent() != null) {
            user = (User) getIntent().getExtras().get(MainActivity.userKey);
            threadsListClass = (ThreadsList) getIntent().getExtras().get(MainActivity.threadsKey);

            name.setText(user.getUser_fname() + " " + user.getUser_lname());

            recyclerView1 = findViewById(R.id.recyclerView);
            adapter1 = new ThreadAdapter(threadsListClass, user);
            layoutManager1 = new LinearLayoutManager(MessageThreadsActivity.this);
            recyclerView1.setLayoutManager(layoutManager1);
            recyclerView1.setAdapter(adapter1);

        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessageThreadsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.isConnected(MessageThreadsActivity.this)) {
//                    Toast.makeText(MessageThreadsActivity.this, "Yes Internet", Toast.LENGTH_SHORT).show();
                    String text = newThreadTitle.getText().toString();
                    if (text.isEmpty() || text.equals("")) {
                        newThreadTitle.setError("Empty thread title");
                    } else {
                        new CallAPI(MessageThreadsActivity.this).addThread(user, newThreadTitle.getText().toString());
                    }

                } else {
                    Toast.makeText(MessageThreadsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToChat(Threads currentThread){
        Intent i = new Intent(MessageThreadsActivity.this, ChatroomActivity.class);
        i.putExtra(threadKey, currentThread);
        i.putExtra(MainActivity.userKey, user);

        startActivity(i);
    }

    @Override
    public void handleInfo(final User user, final Object object) {
        threadsListClass = (ThreadsList) object;

//        Log.d("demo","threadlist="+threadsList);
//        Log.d("demo","user="+user);
        MessageThreadsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter1 = new ThreadAdapter(threadsListClass, user);
                layoutManager1 = new LinearLayoutManager(MessageThreadsActivity.this);
                recyclerView1.setLayoutManager(layoutManager1);
                recyclerView1.setAdapter(adapter1);
            }
        });
    }
}
