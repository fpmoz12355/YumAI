package ba.fpmoz.yumai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4500;

    // Variables
    Animation left_splash, right_splash, bottom_splash;
    ImageView imageViewAI, imageViewYum;

    TextView sloganTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);



        //Animations
        left_splash = AnimationUtils.loadAnimation(this, R.anim.left_splash);
        right_splash = AnimationUtils.loadAnimation(this, R.anim.right_splash);
        bottom_splash = AnimationUtils.loadAnimation(this, R.anim.bottom_splash);

        //Hooks
        imageViewAI = findViewById(R.id.imageViewAI);
        imageViewYum = findViewById(R.id.imageViewYum);
        sloganTv = findViewById(R.id.sloganTv);

        imageViewAI.setAnimation(right_splash);
        imageViewYum.setAnimation(left_splash);
        sloganTv.setAnimation(bottom_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (SplashActivity.this,WelcomeActivity.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_SCREEN);


    }
}