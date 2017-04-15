package com.vsquad.projects.govorillo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognition;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.RecognizerListener;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PersonalText extends AppCompatActivity
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


    public EditText edit1;
    //public EditText edit2;
    public ImageView MainBtn;

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

    RecognizerListener listener = new RecognizerListener() {
        @Override
        public void onRecordingBegin(Recognizer recognizer) {
            //Toast.makeText(MainActivity.this, "onRecordingBegin", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeechDetected(Recognizer recognizer) {
            isrunning = true;
            isSpeechOn = true;
            whatButton = 10;
            runTimer();
        }

        @Override
        public void onSpeechEnds(Recognizer recognizer) {
            //Toast.makeText(MainActivity.this, "Запись окончена", Toast.LENGTH_SHORT).show();
            if (isSpeechOn == true) {
                isSpeechOn = false;
                isrunning = false;
                recognizer.finishRecording();
            }
        }

        @Override
        public void onRecordingDone(Recognizer recognizer) {
            //Toast.makeText(MainActivity.this, "onRecordingDone", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSoundDataRecorded(Recognizer recognizer, byte[] bytes) {

        }

        @Override
        public void onPowerUpdated(Recognizer recognizer, float v) {

        }

        @Override
        public void onPartialResults(Recognizer recognizer, Recognition recognition, boolean b) {
            //Toast.makeText(MainActivity.this, "Partial results " + recognition.getBestResultText(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRecognitionDone(Recognizer recognizer, Recognition recognition) {
            mStreamId = VEnd.play(mSoundId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
            PersonalText.this.onRecognitionDone(recognizer, recognition);
        }

        @Override
        public void onError(Recognizer recognizer, Error error) {
            //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    };

    Recognizer recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, listener, true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_text);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit1 = (EditText) findViewById(R.id.editText); // текст
        //edit2 = (EditText) findViewById(R.id.editText2); // title
        MainBtn = (ImageView) findViewById(R.id.MainBtn);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            xp = bundle.getInt("xp");
        }


        VStart = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        VEnd = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        VStart.load(this, R.raw.okgglv, 1);
        VEnd.load(this, R.raw.okggle, 1);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        leftVolume = curVolume / maxVolume;
        rightVolume = curVolume / maxVolume;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
    }

    public void clearText(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }


    }

    public void startMicro(View view) {
        if (whatButton == 10) {
            isSpeechOn = false;
            whatButton = 1;
            MainBtn.setImageResource(R.drawable.mainmicro);
            isrunning = false;
            recognizer.finishRecording();
        } else {
            if (whatButton == 1) {
                seconds = 0;
                myseconds = 0;
                if (view.getId() == R.id.MainBtn) {
                    TextView result = (TextView) findViewById(R.id.resultText);
                    MainBtn.setImageResource(R.drawable.stop);
                    startRec();
                }
            }
        }
    }

    private void resetRecognizer() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer = null;
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
        getMenuInflater().inflate(R.menu.personal_text, menu);
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

    @TargetApi(Build.VERSION_CODES.M)
    public void startRec() {
        final Context context = getApplicationContext();
        if (context == null) {
            return;
        }
        if (ContextCompat.checkSelfPermission(context, RECORD_AUDIO) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{RECORD_AUDIO}, 1);
        } else {
            mStreamId = VStart.play(mSoundId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
            resetRecognizer();
            recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, listener, true);
            recognizer.start();
        }
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

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                myseconds++;
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secon = seconds % 60;
                String time = String.format("%d:%02d:%02d", hours, minutes, secon);
                //chronometr.setText(time);
                if (isrunning) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onRecognitionDone(Recognizer recognizer, Recognition results) {

        s = results.getBestResultText().toLowerCase();

        mas = s.split(" ");
        if (mas[0] == "" && mas.length == 1) {
            Toast.makeText(this, "Распознать Вашу речь не удалось.", Toast.LENGTH_SHORT).show();
            whatButton = 1;
            MainBtn.setImageResource(R.drawable.mainmicro);
        } else {

            textspeed = (double) mas.length / (double) myseconds;

            // ПОДСЧЁТ ПОВТОРОВ
            for (int k = 0; k < mas.length - 1; k++) {
                for (int l = k + 1; l < mas.length - 1; l++) {
                    if (mas[k].startsWith(mas[l])) repeats++;
                }
            }

            // ПОДСЧЁТ ПАРАЗИТОВ
            for (int k = mas.length - 1; k > 0; k--) {
                int check = 0;
                if (mas[k - 1].endsWith(",")) {
                    check = 1;
                }
                if (mas[k].equals("а") && check == 0) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("короче") || mas[k].startsWith("кароче")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("однако")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("это")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("типа") || mas[k].startsWith("типо")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].equals("бы") && mas[k - 1].equals("как")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].equals("самое") && mas[k - 1].equals("это")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].equals("сказать") && mas[k - 1].equals("как")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].equals("общем") && mas[k - 1].equals("в")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("знаешь")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("ну") && mas[k].length() == 2) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("есть") && mas[k - 1].equals("то")) {
                    errors_in_text++;
                }
                if (mas[k].equals("так") && mas[k - 1].equals("сказать")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("понимаешь")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("собственно")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("принципе") && mas[k - 1].equals("в")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("допустим")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("например")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("слушай")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("говоря") && mas[k - 1].startsWith("собственно")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("кстати")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("вообще")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("кажется")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("вероятно")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("значит")) {
                    errors_in_text++;
                    continue;
                }
                if ((mas[k].equals("деле") && mas[k - 1].equals("самом"))) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].equals("просто") && check == 0) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("вот") && mas[k].length() == 3) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("конкретно")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("ладно")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("блин")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("так")) {
                    errors_in_text++;
                    continue;
                }
                if (mas[k].startsWith("походу")) {
                    errors_in_text++;
                    continue;
                }
            }
            //System.out.println("Время: " + myseconds);
            whatButton=1;
            seconds = 0;
            seconds = 0;
            myseconds = 0;
            Intent toResults = new Intent(this, Results.class);
            toResults.putExtra("errors_in_text", errors_in_text);
            toResults.putExtra("repeats", repeats);
            toResults.putExtra("textspeed", textspeed);
            toResults.putExtra("textlength", mas.length);
            toResults.putExtra("xp", xp);
            toResults.putExtra("isfirst", true);
            toResults.putExtra("text", s);
            startActivity(toResults);
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
