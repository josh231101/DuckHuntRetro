package app.android.joshjosh.duckhuntretro.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import app.android.joshjosh.duckhuntretro.R;

public class RankingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RankingActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer,new UserRankingFragment())
                .commit();

    }

}
