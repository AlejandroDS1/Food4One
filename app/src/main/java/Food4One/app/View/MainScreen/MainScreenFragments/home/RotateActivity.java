package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Food4One.app.R;

public class RotateActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        imageView = findViewById(R.id.imageViewWheel);


    }

    public void spin(View view){
        Animation rotateImage = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        imageView.startAnimation(rotateImage);
    }
}
