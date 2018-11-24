package mad.sis.uncc.inclass07;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ChatroomActivity extends AppCompatActivity implements InterfaceFunctions {

    TextView chatName;
    ImageButton homeBtn, sendBtn;
    EditText messageText;

    User user;
    Threads singleThread;
    MessagesList messagesList;

    public RecyclerView recyclerView2;
    public RecyclerView.Adapter adapter2;
    public RecyclerView.LayoutManager layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        setTitle("Chatroom");

        chatName = findViewById(R.id.labelThreadName);
        homeBtn = findViewById(R.id.homeButton);
        sendBtn = findViewById(R.id.sendButton);
        messageText = findViewById(R.id.editMessage);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent() != null) {
            Log.d("demo", "in chat room");
            singleThread = (Threads) getIntent().getExtras().get(MessageThreadsActivity.threadKey);
            chatName.setText(singleThread.title);
            user = (User)getIntent().getExtras().get(MainActivity.userKey);
            new CallAPI(this).callMessages(user, singleThread.id);

            recyclerView2 = findViewById(R.id.recyclerViewChat);

        }
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.isConnected(ChatroomActivity.this)) {
                    String text = messageText.getText().toString();
                    if (text.isEmpty() || text.equals("")) {
                        messageText.setError("Empty message");
                    } else {
                        new CallAPI(ChatroomActivity.this).addMessage(user, messageText.getText().toString(), singleThread.id);
                    }
                } else {
                    Toast.makeText(ChatroomActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void handleInfo(final User user, Object object) {
        Log.d("demo", "in chat handle");
        this.user = user;
        messagesList = (MessagesList) object;
        Log.d("demo", "in chat " + messagesList.toString());

        ChatroomActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter2 = new MessageAdapter(messagesList, user, singleThread);
                layoutManager2 = new LinearLayoutManager(ChatroomActivity.this);
                recyclerView2.setLayoutManager(layoutManager2);
                recyclerView2.setAdapter(adapter2);
            }
        });
    }
}
