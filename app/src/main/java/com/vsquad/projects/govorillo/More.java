package com.vsquad.projects.govorillo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
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

public class More extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sPref;
    final String SAVED_XP = "saved_xp";
    private ImageView back;
    private TextView tv1;
    private TextView tv2;
    private int xp = 0;
    private int errors_in_text = 0;
    private double textspeed = 0;
    private int repeats = 0;
    private int textlength = 0;
    private boolean isfirst = false;
    public int newxp = 0;
    private double result = 0.0;
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            xp = bundle.getInt("xp");
            errors_in_text = bundle.getInt("errors_in_text");
            textspeed = bundle.getDouble("textspeed");
            repeats = bundle.getInt("repeats");
            textlength = bundle.getInt("textlength");
            isfirst = bundle.getBoolean("isfirst");
            s = bundle.getString("text");
            result = bundle.getDouble("res");
        }
        tv1.setText("Паразитов: " + errors_in_text + "\nПовторов: " + repeats + "\nСлов в минуту: " + (int) Math.ceil(textspeed * 60));

        String[] mas = s.split(" ");
        // НАЧАЛО ОБРАБОТКИ ТЕКСТА
        final SpannableStringBuilder text = new SpannableStringBuilder();
        final SpannableStringBuilder text1 = new SpannableStringBuilder();
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        //final Typeface = Typeface.BOLD;
        // ПОДСЧЁТ ПОВТОРОВ
/*
        for (int k = 0; k < mas.length - 1; k++) {
            for (int l = k + 1; l < mas.length - 1; l++) {
                if (mas[k].startsWith(mas[l])){
                    text1.append(mas[l]).setSpan(boldSpan,0,mas[l].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    text.append(" "+text1);
                    text1.delete(0, text1.length());
                }
            }
        }
*/
        // ПОДСЧЁТ ПАРАЗИТОВ
        for (int k = mas.length - 1; k > 0; k--) {
            int nowl = 0;
            int check = 0;
            if (mas[k - 1].endsWith(",")) {
                check = 1;
            } else
            if (mas[k].equals("а") && check == 0) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            } else
            if (mas[k].startsWith("короче") || mas[k].startsWith("кароче")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("однако")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("это")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("типа") || mas[k].startsWith("типо")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].equals("бы") && mas[k - 1].equals("как")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].equals("самое") && mas[k - 1].equals("это")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].equals("сказать") && mas[k - 1].equals("как")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].equals("общем") && mas[k - 1].equals("в")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("знаешь")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("ну") && mas[k].length() == 2) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("есть") && mas[k - 1].equals("то")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].equals("так") && mas[k - 1].equals("сказать")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("понимаешь")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("собственно")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("принципе") && mas[k - 1].equals("в")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("допустим")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("например")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("слушай")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("говоря") && mas[k - 1].startsWith("собственно")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("кстати")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("вообще")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("кажется")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("вероятно")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("значит")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if ((mas[k].equals("деле") && mas[k - 1].equals("самом"))) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].equals("просто") && check == 0) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("вот") && mas[k].length() == 3) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("конкретно")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("ладно")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("блин")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("так")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }else
            if (mas[k].startsWith("походу")) {
                nowl = text.length();
                text.append(" "+text1);
                text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            } else if (1==1){
                int checkp=0;
                for (int l=0; l < k-1; l++) {
                    if (mas[k].startsWith(mas[l])) {
                        nowl = text.length();
                        text.append(" "+text1);
                        text.setSpan(boldSpan,nowl,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        checkp=1;
                        break;
                    }
                }
                if (checkp==0){
                    text.append(" "+mas[k]);
                }
                checkp=0;
                continue;
            }
        }


        tv2.setText("Надиктованный текст:\n" + s);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
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

    public void toRes(View view) {
        Intent toRes = new Intent(this, Results.class);
        toRes.putExtra("xp", xp);
        toRes.putExtra("textlength", textlength);
        toRes.putExtra("repeats", repeats);
        toRes.putExtra("textspeed", textspeed);
        toRes.putExtra("errors_in_text", errors_in_text);
        toRes.putExtra("xp", xp);
        toRes.putExtra("text", s);
        toRes.putExtra("isfirst", false);
        toRes.putExtra("res", result);
        startActivity(toRes);
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
        getMenuInflater().inflate(R.menu.more, menu);
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
