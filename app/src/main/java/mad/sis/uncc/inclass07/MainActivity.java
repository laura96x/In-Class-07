package mad.sis.uncc.inclass07;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements InterfaceFunctions{

    EditText email;
    EditText password;
    Button login, signup;
    public static CallAPI callAPI;

    public static String userKey = "user";
    public static String threadsKey = "threadsListClass";
    User user;
    ThreadsList threadsListClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(R.string.title);

        email = findViewById(R.id.emaileditText);
        password = findViewById(R.id.editText2);
        login = findViewById(R.id.loginbutton);
        signup = findViewById(R.id.signupbutton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "clicked login");
                if (isConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "Yes Internet", Toast.LENGTH_SHORT).show();
                    String emailString = email.getText().toString();
                    String passwordString = password.getText().toString();
                    if (emailString.isEmpty() || passwordString.isEmpty()) {
                        if (emailString.isEmpty()) {
                            email.setError("Empty email value");
                        }
                        if (passwordString.isEmpty()) {
                            password.setError("Empty password value");
                        }
//                        Toast toast = Toast.makeText(MainActivity.this, "zInvalid log in", Toast.LENGTH_SHORT);
//                        toast.show();
                    } else {
                        new CallAPI(MainActivity.this).login(new User(emailString, passwordString), MainActivity.this);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "clicked signup");
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

    }

    public static boolean isConnected(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void handleInfo(User user2, Object object) {
        this.user = user2;
        threadsListClass = (ThreadsList) object;

//        Log.d("demo", "in main java token");
//        Log.d("demo", "in main " + user.toString());
//        Log.d("demo", "in main " + threadsListClass.toString());

//        if (user.status.equals("error")) {
//            Log.d("demo", "null");
//        } else {
//            Log.d("demo", user.token);
            SharedPreferences.Editor sharedPre = getSharedPreferences("Message Threads App", this.MODE_PRIVATE).edit();
            sharedPre.putString("token", user.token);
            sharedPre.commit();

            Intent i = new Intent(this, MessageThreadsActivity.class);
            i.putExtra(userKey, user);
            i.putExtra(threadsKey, threadsListClass);
            startActivity(i);
            finish();
//        }
    }
}
