package intranet.client.android.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import intranet.client.R;
import intranet.client.android.asynctasks.CreateAnnouncementTask;
import intranet.client.android.delegates.CreateAnnouncementTaskDelegate;

public class CreateAnnouncementActivity extends AppCompatActivity implements CreateAnnouncementTaskDelegate {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText titleEditText;
    private EditText messageEditText;
    private Button saveButton;
    private ProgressDialog progressDialog;
    private CreateAnnouncementTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        titleEditText = (EditText) findViewById(R.id.input_title);
        messageEditText = (EditText) findViewById(R.id.input_message);
        saveButton = (Button) findViewById(R.id.btn_save);

        task = new CreateAnnouncementTask(this, this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    public void save() {
        if (!validate()) {
            onAnnouncementCreateFormInvalid();
            return;
        }

        saveButton.setEnabled(false);

        progressDialog = new ProgressDialog(CreateAnnouncementActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.login_progress_message));
        progressDialog.show();

        String title = titleEditText.getText().toString();
        String message = messageEditText.getText().toString();

        task.execute(title, message);
    }

    void onAnnouncementCreateFormInvalid() {

    }

    @Override
    public void onAnnouncementCreatedSuccess() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onAnnouncementCreatedFinished();
                finish();
            }
        });
    }

    @Override
    public void onAnnouncementCreatedUnauthorized() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onAnnouncementCreatedFinished();
                Toast.makeText(getBaseContext(), R.string.create_announcement_unauthorized_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAnnouncementCreatedFailed() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onAnnouncementCreatedFinished();
                Toast.makeText(getBaseContext(), R.string.create_announcement_failed_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onAnnouncementCreatedFinished() {
        if ( progressDialog != null ) {
            progressDialog.dismiss();
        }
        saveButton.setEnabled(true);
    }

    public boolean validate() {
        return validateTitle() && validateMessage();
    }

    public boolean validateMessage() {
        String message = messageEditText.getText().toString();
        if (message.isEmpty()) {
            messageEditText.setError(getString(R.string.password_error_size));
            return false;
        }
        messageEditText.setError(null);
        return true;
    }
    public boolean validateTitle() {
        String title = titleEditText.getText().toString();
        if (title.isEmpty()) {
            titleEditText.setError(getString(R.string.username_error_invalid));
            return false;
        }
        titleEditText.setError(null);
        return true;
    }
}
