package info.akshaysadarangani.ILoveZappos.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import info.akshaysadarangani.ILoveZappos.R;

/**
 * Created by Akshay on 2/9/2017.
 */

public class SplashActivity extends AppCompatActivity {

    ShimmerTextView app,author;
    Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        app = (ShimmerTextView) findViewById(R.id.splash_text);
        author = (ShimmerTextView) findViewById(R.id.splash_name);
        shimmer = new Shimmer();
        shimmer.start(app);
        shimmer.start(author);
        shimmer.setRepeatCount(0)
                .setDuration(3000)
                .setDirection(Shimmer.ANIMATION_DIRECTION_RTL)
                .setAnimatorListener(new Animator.AnimatorListener(){
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


    }

}
