package com.vsquad.projects.govorillo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;
import android.widget.TextView;

import ru.yandex.speechkit.Recognition;

public class Modernize extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private int errors_in_text = 0;
    private int xp = 0;
    private int newxp;
    private TextView resultTEXT;
    private TextView badwords;
    //private Chronometer golosChronometr;
    private int seconds;
    private String s = new String();
    private String[] mas = new String[s.length()];
    private boolean isrunning;
    private TextView chronometr;
    private int myseconds = 0;
    private double textspeed = 0.0;
    private TextView tvspeed;
    private Spinner myspinner;
    private String selectedspinner;
    private TextView tvreglament;
    private int sinnergid = 0;
    private int repeats = 0;
    private int whatButton = 1;
    private ImageView MainButton;
    private Recognition results;
    private int REQUEST_PERMISSION_CODE = 1;
    boolean isSpeechOn = false;

    public TextView title;
    public TextView subtitle;
    public ImageView MainBtn;
    private SoundPool VStart, VEnd;
    private int mSoundId = 1;
    private int mStreamId;
    AudioManager audioManager;
    float curVolume;
    float maxVolume;
    float leftVolume;
    float rightVolume;
    int priority = 1;
    int no_loop = 0;
    float normal_playback_rate = 1f;

    private int timelimitbyuser = 0;
    // 0 <==> 4
    // 0 - без регламента
    // 1 - 15 секунд
    // 2 - 30 секунд
    // 3 - 1 минута
    // 4 - 5 минут

    private SharedPreferences sPref;
    final String SAVED_XP = "saved_xp";
    private String ApiKey = "47e6dff5-f7f8-4a8f-afef-cb8a3a08f836";

    private TextView titletext;
    private TextView modern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_modernize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        titletext = (TextView) findViewById(R.id.titletextmodern);
        modern = (TextView) findViewById(R.id.mytitlemodern);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            xp = bundle.getInt("xp");
        }

        //Паразиты
        String[] baseOne = {
                "Появление в речи слов-паразитов чаще всего происходит при нечеткой формулировке мыслей либо при потере нити повествования. Если вы вдруг забыли необходимое слово, то лучше просто глубоко вдохнуть и мысленно постараться взять себя в руки. ",
                "Если вы вдруг почувствовали большой соблазн вставить в свою речь какое-нибудь ненужное словечко, необходимо проконтролировать себя, сделав большой вдох. Это действие приведет к ненамеренной паузе, которую можно использовать для дальнейшего построения ваших будущих фраз. Эта пауза будет выглядеть намного лучше, чем употребление таких слов как: короче, это самое и т. д.",
                "Необходимо приучить себя делать паузу в нужном месте. Если раньше вместо этого у вас возникал поток ненужных фраз и словосочетаний необходимо научиться «придерживать коней», ведь, согласно известной поговорке, молчание – золото. При возникновении неловкой ситуации или когда тема диалога полностью себя исчерпала, на помощь придет минутная тишина.",
                "Если сосредоточить внимание на негативе, количество слов-паразитов может только возрасти. Постарайтесь сфокусироваться на чем-то положительном. Например, произнесите вслух: “Я превосходно справляюсь с паузами”. Эта фраза с большим количеством согласных звуков не только заставит вашу речь немного замедлиться, но и сделает акцент на том, что вам хорошо удается.",
                "Эти словечки пробираются в начало наших фраз, становясь \"взлетной полосой\" для мысли, которая еще не успела оформиться. Чаще всего в такой роли выступают всевозможные “короче”, “честно говоря”, “типа”, “вообще”. Без них речь звучит весомее и продуманнее, будто бы нам не нужен “разгон” в самом начале."
        };
        //Дикция
        String[] baseTwo = {
                "Больше читайте произведения классиков.",
                "Слушайте записи стихов опытных дикторов.",
                "Общайтесь с представителями разных социальных групп.",
                "Тренируйтесь описывать несколькими предложениями любые предметы.",
                "Тренируйтесь описывать несколькими предложениями любые предметы в нашем приложении."
        };
        //Повторы
        String[] baseThree = {
                "Пойте как можно больше вслух, следите за тем, чтобы внятно и четко пропевать окончания слов. Сходите, если не будете стесняться, в караоке-бар. Выберите песни, медленные по ритму, русский народные, например, и привыкайте к этому размеренному темпу. Если на пение нет желания или возможности, незаметно ото всех отбивайте ритм ногой во время разговора, старайтесь говорить движениям в такт и не сбиваться с ритма, который вы задумали.",
                "Разработайте мышцы языка посредством выполнения простых логопедических упражнений. Придавайте формы лодочки языку, облизывайте широко губы, будто пытаетесь слизать варенье. Сворачивайте его в трубочку, при этом двигайте им назад и вперед при открытом рте. Доставайте до неба кончиком языка. Ежедневно повторяйте все движения раз по пятнадцать.",
                "Пойте как можно больше вслух, следите за тем, чтобы внятно и четко пропевать окончания слов. Если на пение нет желания или возможности, незаметно ото всех отбивайте ритм ногой во время разговора, старайтесь говорить движениям в такт и не сбиваться с этого ритма.",
                "Разработайте мышцы языка посредством выполнения простых логопедических упражнений. Придавайте формы лодочки языку, облизывайте широко губы, будто пытаетесь слизать варенье.",
                "Откройте книгу и читайте ее на время вслух. На то, чтобы прочитать стандартную страницу книги, у вас должно уходить не менее трех минут."
        };


        int randomnum = (int) Math.ceil(Math.random() * 3) - 1;
        int randomnum2 = (int) Math.ceil(Math.random() * 5) - 1;

        if (randomnum==0){
            modern.setText("Паразиты");
            titletext.setText(baseOne[randomnum2]);
        } else
        if (randomnum==1){
            modern.setText("Дикция");
            titletext.setText(baseTwo[randomnum2]);
        } else
        if (randomnum==2){
            modern.setText("Повторы");
            titletext.setText(baseThree[randomnum2]);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
    }

    public void changeText(View view) {

        //Паразиты
        String[] baseOne = {
                "Появление в речи слов-паразитов чаще всего происходит при нечеткой формулировке мыслей либо при потере нити повествования. Если вы вдруг забыли необходимое слово, то лучше просто глубоко вдохнуть и мысленно постараться взять себя в руки. ",
                "Если вы вдруг почувствовали большой соблазн вставить в свою речь какое-нибудь ненужное словечко, необходимо проконтролировать себя, сделав большой вдох. Это действие приведет к ненамеренной паузе, которую можно использовать для дальнейшего построения ваших будущих фраз. Эта пауза будет выглядеть намного лучше, чем употребление таких слов как: короче, это самое и т. д.",
                "Необходимо приучить себя делать паузу в нужном месте. Если раньше вместо этого у вас возникал поток ненужных фраз и словосочетаний необходимо научиться «придерживать коней», ведь, согласно известной поговорке, молчание – золото. При возникновении неловкой ситуации или когда тема диалога полностью себя исчерпала, на помощь придет минутная тишина.",
                "Если сосредоточить внимание на негативе, количество слов-паразитов может только возрасти. Постарайтесь сфокусироваться на чем-то положительном. Например, произнесите вслух: “Я превосходно справляюсь с паузами”. Эта фраза с большим количеством согласных звуков не только заставит вашу речь немного замедлиться, но и сделает акцент на том, что вам хорошо удается.",
                "Эти словечки пробираются в начало наших фраз, становясь \"взлетной полосой\" для мысли, которая еще не успела оформиться. Чаще всего в такой роли выступают всевозможные “короче”, “честно говоря”, “типа”, “вообще”. Без них речь звучит весомее и продуманнее, будто бы нам не нужен “разгон” в самом начале."
        };
        //Дикция
        String[] baseTwo = {
                "Больше читайте произведения классиков.",
                "Слушайте записи стихов опытных дикторов.",
                "Общайтесь с представителями разных социальных групп.",
                "Тренируйтесь описывать несколькими предложениями любые предметы.",
                "Тренируйтесь описывать несколькими предложениями любые предметы в нашем приложении."
        };
        //Повторы
        String[] baseThree = {
                "Пойте как можно больше вслух, следите за тем, чтобы внятно и четко пропевать окончания слов. Сходите, если не будете стесняться, в караоке-бар. Выберите песни, медленные по ритму, русский народные, например, и привыкайте к этому размеренному темпу. Если на пение нет желания или возможности, незаметно ото всех отбивайте ритм ногой во время разговора, старайтесь говорить движениям в такт и не сбиваться с ритма, который вы задумали.",
                "Разработайте мышцы языка посредством выполнения простых логопедических упражнений. Придавайте формы лодочки языку, облизывайте широко губы, будто пытаетесь слизать варенье. Сворачивайте его в трубочку, при этом двигайте им назад и вперед при открытом рте. Доставайте до неба кончиком языка. Ежедневно повторяйте все движения раз по пятнадцать.",
                "Пойте как можно больше вслух, следите за тем, чтобы внятно и четко пропевать окончания слов. Если на пение нет желания или возможности, незаметно ото всех отбивайте ритм ногой во время разговора, старайтесь говорить движениям в такт и не сбиваться с этого ритма.",
                "Разработайте мышцы языка посредством выполнения простых логопедических упражнений. Придавайте формы лодочки языку, облизывайте широко губы, будто пытаетесь слизать варенье.",
                "Откройте книгу и читайте ее на время вслух. На то, чтобы прочитать стандартную страницу книги, у вас должно уходить не менее трех минут."
        };


        int randomnum = (int) Math.ceil(Math.random() * 3) - 1;
        int randomnum2 = (int) Math.ceil(Math.random() * 5) - 1;

        if (randomnum==0){
            modern.setText("Паразиты");
            titletext.setText(baseOne[randomnum2]);
        } else
        if (randomnum==1){
            modern.setText("Дикция");
            titletext.setText(baseTwo[randomnum2]);
        } else
        if (randomnum==2){
            modern.setText("Повторы");
            titletext.setText(baseThree[randomnum2]);
        }

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
        getMenuInflater().inflate(R.menu.modernize, menu);
        return true;
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

