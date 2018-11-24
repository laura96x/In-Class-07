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

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {
    ThreadsList threads;
    User user;
    MessageThreadsActivity msgcntx;

    public ThreadAdapter(ThreadsList threads, User user) {
        this.threads = threads;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.thread_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ct = (MessageThreadsActivity) viewGroup.getContext();
        msgcntx = (MessageThreadsActivity) viewGroup.getContext();
        viewHolder.user = this.user;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Threads eachThread = threads.threads.get(i);
        viewHolder.currentThread = eachThread;
        viewHolder.textName.setText(eachThread.title);
        if (user.user_id.equals(eachThread.user_id)) {
            viewHolder.buttonDelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.buttonDelete.setVisibility(View.INVISIBLE);
        }

        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "click delete " + eachThread.title);

                if (MainActivity.isConnected(msgcntx)) {
//                    Toast.makeText(msgcntx, "adapter Yes Internet", Toast.LENGTH_SHORT).show();
                    new CallAPI(msgcntx).deleteThread(user, eachThread.id);
                } else {
                    Toast.makeText(msgcntx, "adapter No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return threads.threads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        ImageView buttonDelete;
        Threads currentThread;
        MessageThreadsActivity ct;
        User user;
        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.threadNameView);
            buttonDelete = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demo", "clicky");
                    String threadName = ((TextView)v.findViewById(R.id.threadNameView)).getText().toString();
                    Log.d("demo", ""+ currentThread.title);

                    msgcntx.goToChat(currentThread);
                }
            });
        }
    }
}
