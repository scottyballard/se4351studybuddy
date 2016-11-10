package se4351.studybuddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;



public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button Register = (Button) findViewById(R.id.bRegister);
        final Button bLogin = (Button) findViewById(R.id.bLogin);

        //Initializes Login api, Needs to be change to whatever the final db connection string is
        final Login login = new Login("");


        Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                boolean result = Login.register(username, password);

                if(!result)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("That username is already taken")
                            .setNegativeButton("Try again", null)
                            .create().show();

                }
            }
        });
        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                boolean result = Login.login(username, password);

                if(!result)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Login Failed")
                            .setNegativeButton("Try again", null)
                            .create().show();

                }
                else
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    LoginActivity.this.startActivity(intent);
                }

            }
        });

    }

}
