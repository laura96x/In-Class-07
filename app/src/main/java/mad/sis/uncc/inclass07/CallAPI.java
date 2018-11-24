package mad.sis.uncc.inclass07;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallAPI {

    ThreadsList threadsList;
    MessagesList messagesList;

    public static final String URL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/";
    private final OkHttpClient client = new OkHttpClient();

    InterfaceFunctions activity;
    Activity context;

    public CallAPI(InterfaceFunctions activity) {
        this.activity = activity;
    }

    public void login(User user, Activity context2) {
        context = context2;
        Log.d("demo", "in callAPI.login");
        RequestBody formBody = new FormBody.Builder()
                .add("email", user.getUser_email())
                .add("password", user.getPassword())
                .build();
        Request request = new Request.Builder()
                .url(URL + "login")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo", "in callAPI.login onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
//                Log.d("demo", "login response " + body);

                final User user = new Gson().fromJson(body, User.class);
//                Log.d("demo", "login user " + user.toString());

                if(user.status.equals("error")){
                    context.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context, user.message, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }));
                } else {
                    callThread(user);
                }
            }
        });
    }

    public void signup(User user, Activity context3) {
        context = context3;
        Log.d("demo", "in callAPI.signup");
        RequestBody formBody = new FormBody.Builder()
                .add("email", user.getUser_email())
                .add("password", user.getPassword())
                .add("fname", user.getUser_fname())
                .add("lname", user.getUser_lname())
                .build();
        Request request = new Request.Builder()
                .url(URL + "signup")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("demo", "signup: thread response " + body);

                final User user = new Gson().fromJson(body, User.class);
                Log.d("demo", "signup: user " + user.toString());

                if (user.status.equals("error")) {
                    context.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context, user.message, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }));
                } else {
                    callThread(user);
                }
            }
        });
    }

    public void callThread(final User user)  {
        Log.d("demo", "in callAPI.callThread");
        Request request = new Request.Builder()
                .url(URL+"thread")
                .header("Authorization", "BEARER " + user.getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
//                Log.d("demo", "thread response " + body);

                threadsList = new Gson().fromJson(body, ThreadsList.class);
//                Log.d("demo", "thread call" + threads.toString());

                activity.handleInfo(user, threadsList);
            }
        });
    }

    public void deleteThread(final User user, String id)  {
        Log.d("demo", "in callAPI.deleteThread");
        Log.d("demo", "delete " + id);
        Request request = new Request.Builder()
                .url(URL + "thread/delete/" + id)
                .header("Authorization", "BEARER " + user.getToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("demo", "delete response " + body);
                callThread(user);
            }
        });
    }

    public void addThread(final User user, String addTitle)  {
        Log.d("demo", "in callAPI.addThread");
        RequestBody formBody = new FormBody.Builder()
                .add("title", addTitle)
                .build();
        Request request = new Request.Builder()
                .url(URL + "thread/add")
                .header("Authorization", "BEARER " + user.getToken())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("demo", "add response " + body);
                callThread(user);
            }
        });
    }

    public void callMessages(final User user, String id)  {
        Log.d("demo", "in callAPI.callMessages");
        Request request = new Request.Builder()
                .url(URL+"messages/" + id)
                .header("Authorization", "BEARER " + user.getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("demo", "msg response " + body);

                messagesList = new Gson().fromJson(body, MessagesList.class);
                Log.d("demo", "msg call " + messagesList.toString());

                activity.handleInfo(user, messagesList);
            }
        });
    }

    public void deleteMessage(final User user, final String id, final Threads threads)  {
        Log.d("demo", "in callAPI.deleteMessage");
        Log.d("demo", "delete " + id);
        Request request = new Request.Builder()
                .url(URL + "message/delete/" + id)
                .header("Authorization", "BEARER " + user.getToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("demo", "delete response " + body);
                callMessages(user, threads.id);
            }
        });
    }

    public void addMessage(final User user, String addMessage, final String id)  {
        Log.d("demo", "in callAPI.addMessage");
        RequestBody formBody = new FormBody.Builder()
                .add("message", addMessage)
                .add("thread_id", id)
                .build();
        Request request = new Request.Builder()
                .url(URL + "message/add")
                .header("Authorization", "BEARER " + user.getToken())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("demo", "add response " + body);
                callMessages(user, id);
            }
        });
    }
}
