package Food4One.app.View.Authentification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import Food4One.app.R;


public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_help);
    }

}