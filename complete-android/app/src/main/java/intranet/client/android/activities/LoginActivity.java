package intranet.client.android.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.grails.springsecurityrest.GrailsApi;
import org.grails.springsecurityrest.GrailsApiLoginListener;

import intranet.client.R;
import intranet.client.network.Constants;

public class LoginActivity  extends AppCompatActivity implements GrailsApiLoginListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.input_email);
        passwordEditText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.login_progress_message));
        progressDialog.show();

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        GrailsApi.login(this, Constants.GRAILS_APP_URL, username, password, this);

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    public void onLoginSuccess() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoginFormAuthenticationFinished();
                finish();
            }
        });

    }

    @Override
    public void onLoginUnauthorized() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoginFormAuthenticationFinished();
                Toast.makeText(getBaseContext(), R.string.login_unauthorized_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLoginFailed() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoginFormAuthenticationFinished();
                Toast.makeText(getBaseContext(), R.string.login_failed_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onLoginFormAuthenticationFinished() {
        if ( progressDialog != null ) {
            progressDialog.dismiss();
        }
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
            usernameEditText.setError(getString(R.string.username_error_invalid));
            valid = false;
        } else {
            usernameEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordEditText.setError(getString(R.string.password_error_size));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}
