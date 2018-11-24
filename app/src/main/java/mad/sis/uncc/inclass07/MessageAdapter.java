package mad.sis.uncc.inclass07;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    MessagesList messagesList;
    User user;
    Threads threads;
    ChatroomActivity chatcntx;

    public MessageAdapter(MessagesList messagesList, User user, Threads threads) {
        this.messagesList = messagesList;
        this.user = user;
        this.threads = threads;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ct = (ChatroomActivity) viewGroup.getContext();
        chatcntx = (ChatroomActivity) viewGroup.getContext();
        viewHolder.user = this.user;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Messages eachMessage = messagesList.messages.get(i);
        Log.d("demo", "1chat ada " + eachMessage.toString());
        Log.d("demo", "2chat ada " + eachMessage.message);
        viewHolder.currentMessage = eachMessage;
        viewHolder.messageText.setText(eachMessage.message);
        viewHolder.personText.setText(eachMessage.user_fname + " " + eachMessage.user_lname);

        PrettyTime p = new PrettyTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Log.d("demo", "each date " + eachMessage.created_at);
            Log.d("demo", "parse date " + df.parse(eachMessage.created_at));
            Log.d("demo", "format date " + p.format(df.parse(eachMessage.created_at)));
            viewHolder.timeText.setText(p.format(df.parse(eachMessage.created_at)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (user.user_id.equals(eachMessage.user_id)) {
            viewHolder.imgDeleteMsg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgDeleteMsg.setVisibility(View.INVISIBLE);
        }

        viewHolder.imgDeleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "click delete " + eachMessage.message);

                if (MainActivity.isConnected(chatcntx)) {
//                    Toast.makeText(msgcntx, "adapter Yes Internet", Toast.LENGTH_SHORT).show();
                    new CallAPI(chatcntx).deleteMessage(user, eachMessage.id, threads);
                } else {
                    Toast.makeText(chatcntx, "adapter No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return messagesList.messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, personText, timeText;
        ImageView imgDeleteMsg;
        Messages currentMessage;
        ChatroomActivity ct;
        User user;
        public ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.labelMessage);
            personText = itemView.findViewById(R.id.labelPerson);
            timeText = itemView.findViewById(R.id.labelTime);
            imgDeleteMsg = itemView.findViewById(R.id.imageDeleteMsg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demo", "clicky2");
                }
            });
        }
    }
}
