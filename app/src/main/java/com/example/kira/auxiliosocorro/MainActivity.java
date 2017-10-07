package com.example.kira.auxiliosocorro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.kira.auxiliosocorro.Gillotine.animation.GuillotineAnimation;

public class MainActivity extends AppCompatActivity {
    private static final long RIPPLE_DURATION = 250;

    private Toolbar toolbar;

    private FrameLayout root;

    private View contentHamburger;


    private Button btnIncio,btnRefugio;
    private GuillotineAnimation g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        root=(FrameLayout) findViewById(R.id.root);
        contentHamburger=(View)findViewById(R.id.content_hamburger);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        g= new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
    }

    



}
