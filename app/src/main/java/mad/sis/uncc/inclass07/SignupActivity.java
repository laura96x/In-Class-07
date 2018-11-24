package mad.sis.uncc.inclass07;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity implements InterfaceFunctions{


    User user;
    ThreadsList threadsListClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setTitle(R.string.signup);

        Button signUp = findViewById(R.id.signupbutton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }
    public void signup() {
        EditText firstText = findViewById(R.id.fnameText);
        EditText lastText = findViewById(R.id.lnameText);
        EditText emailText = findViewById(R.id.emailText);
        EditText passwordText = findViewById(R.id.pass1Text);
        EditText repeatPasswordText = findViewById(R.id.pass2Text);

        String firstName = firstText.getText().toString();
        String lastName = lastText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String repeatPassword = repeatPasswordText.getText().toString();

        if(firstName == null || firstName.equals("")) {
            firstText.setError("Enter a valid first name");
            return;
        }
        if(lastName == null || lastName.equals("")) {
            lastText.setError("Enter a valid last name");
            return;
        }
        if(email == null || "".equals(email) || email.length() < 3 || !email.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email");
            return;
        }
        if(password == null || password.equals("")) {
            passwordText.setError("Enter a valid password");
            return;
        }
        if(repeatPassword == null || repeatPassword.equals("")) {
            repeatPasswordText.setError("Enter a valid password");
            return;
        }
        if(!repeatPassword.equals(password)) {
            repeatPasswordText.setError("Passwords do not match");
            return;
        }
        user = new User(email, password);
        user.setUser_fname(firstName);
        user.setUser_lname(lastName);

        if (MainActivity.isConnected(SignupActivity.this)) {
            new CallAPI(this).signup(user, SignupActivity.this);
        } else {
            Toast.makeText(SignupActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }


    public void handleInfo(User user2, Object object) {
        user = user2;
        threadsListClass = (ThreadsList) object;

        Log.d("demo", "in signup");
        SharedPreferences.Editor sharedPre = getSharedPreferences("Message Threads App", this.MODE_PRIVATE).edit();
        sharedPre.putString("token", user.token);
        sharedPre.commit();

        Intent i = new Intent(this, MessageThreadsActivity.class);
        i.putExtra(MainActivity.userKey, user);
        i.putExtra(MainActivity.threadsKey, threadsListClass);
        startActivity(i);
        finish();

    }
}
