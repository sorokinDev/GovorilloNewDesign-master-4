package com.vsquad.projects.govorillo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sPref;
    final String SAVED_XP = "saved_xp";
    public int newxp = 0;
    public int xp = 0;
    TextView DailyS;
    TextView level;
    TextView name;
    ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DailyS = (TextView) findViewById(R.id.DailyS);
        level = (TextView) findViewById(R.id.level);
        name = (TextView) findViewById(R.id.name);
        profileImg = (ImageView) findViewById(R.id.profileimage);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            xp = bundle.getInt("xp");
        }

        if (xp >= 0 && xp < 50) {
            name.setText("Школьник");
            profileImg.setImageResource(R.drawable.schoolman);
            level.setText("Уровень: 1");
        }
        if (xp >= 50 && xp < 500) {
            name.setText("Видеоблогер");
            profileImg.setImageResource(R.drawable.dima);
            level.setText("Уровень: 2");
        }
        if (xp >= 500 && xp < 2500) {
            name.setText("Адвокат");
            profileImg.setImageResource(R.drawable.advokat);
            level.setText("Уровень: 3");
        }
        if (xp >= 2500 && xp < 7500) {
            name.setText("Политик");
            profileImg.setImageResource(R.drawable.putin);
            level.setText("Уровень: 4");
        }
        if (xp >= 7500) {
            name.setText("Оратор");
            profileImg.setImageResource(R.drawable.gandapas);
            level.setText("Уровень: 5");
        }


        String[] masDaily = {
                "Определи сам для себя: каким будет твоё выступление; как ты будешь говорить, двигаться. Какой результат тебе нужен.",
                "Используй правдивые случаи, которые имеют отношение к теме, тем самым твоё выступление запомнится.",
                "Оставайся собой. При выходе на сцену скажи, что ты выступаешь впервые. Это снизит энергетику в зале.",
                "Следи за своим телом: расправленные плечи, поднятый подбородок, решительные жесты.",
                "Вноси максимум смысла в свои слова и минимум воды.",
                "Дышите глубоко – это дает глубокую картину мысли.",
                "Займись тренировкой мимических мышц."
        };
        int randomnum = (int) Math.ceil(Math.random() * masDaily.length) - 1;

        DailyS.setText("Совет:\n" + masDaily[randomnum]);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(SAVED_XP, xp);
        editor.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        sPref = getPreferences(MODE_PRIVATE);
        newxp = sPref.getInt(SAVED_XP, xp);
        if (newxp > xp) {
            xp = newxp;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent toMain = new Intent(this, MainActivity.class);
            toMain.putExtra("xp", xp);
            startActivity(toMain);
        } else if (id == R.id.nav_random) {
            Intent toRandom = new Intent(this, RandomText.class);
            toRandom.putExtra("xp", xp);
            startActivity(toRandom);

        } else if (id == R.id.nav_personaltext) {
            Intent toPersonal = new Intent(this, PersonalText.class);
            toPersonal.putExtra("xp", xp);
            startActivity(toPersonal);

        } else if (id == R.id.nav_profile) {
            Intent toProfile = new Intent(this, Profile.class);
            toProfile.putExtra("xp", xp);
            startActivity(toProfile);
        } else if (id == R.id.nav_partners) {
            Intent toPartners = new Intent(this, Partners.class);
            toPartners.putExtra("xp", xp);
            startActivity(toPartners);
        } else if (id == R.id.nav_fast) {
            Intent toFast = new Intent(this, FastTexts.class);
            toFast.putExtra("xp", xp);
            startActivity(toFast);
        } else if (id == R.id.nav_modern) {
            Intent toModern = new Intent(this, Modernize.class);
            toModern.putExtra("xp", xp);
            startActivity(toModern);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
