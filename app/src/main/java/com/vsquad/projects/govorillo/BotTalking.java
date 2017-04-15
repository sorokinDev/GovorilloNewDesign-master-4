package com.vsquad.projects.govorillo;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognition;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.RecognizerListener;
import ru.yandex.speechkit.Vocalizer;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static ru.yandex.speechkit.Vocalizer.createVocalizer;

public class BotTalking extends AppCompatActivity
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
    private ProgressDialog progress;
    private ImageView MainButton;
    private Recognition results;
    private int REQUEST_PERMISSION_CODE = 1;
    private String bot_answer="";

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
    boolean isSpeechOn = false;
    private TextView chat;
    //RecognizerListener reclistener = new RecognizerListener();


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

        }

        @Override
        public void onSpeechEnds(Recognizer recognizer) {
            //Toast.makeText(MainActivity.this, "Запись окончена", Toast.LENGTH_SHORT).show();

            if (isSpeechOn == true) {
                isSpeechOn = false;
                isrunning = false;
                //mStreamId = VEnd.play(mSoundId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
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
        public void onRecognitionDone(Recognizer recognizer, Recognition recognition)  {
            //mStreamId = VEnd.play(mSoundId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);

            progress.setMessage("Загрузка ответа");


            try {
                BotTalking.this.onRecognitionDone(recognizer, recognition);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        setContentView(R.layout.activity_bot_talking);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //SDKConnection connection =
        //      new SDKConnection(new BOTlibreCredential("8499830136648224440"));
        MainButton = (ImageView) findViewById(R.id.mainbtn);
        chat = (TextView) findViewById(R.id.chat);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

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

    }

    public void startMicro(View view) {
        if (whatButton == 10) {
            isSpeechOn = false;
            whatButton = 1;
            MainButton.setImageResource(R.drawable.mainmicro);
            isrunning = false;
            recognizer.finishRecording();
            progress = new ProgressDialog(BotTalking.this);
            progress.setMessage("Распознавание речи...");
            progress.show();
        } else {
            if (whatButton == 1) {
                seconds = 0;
                seconds = 0;
                myseconds = 0;
                if (view.getId() == R.id.mainbtn) {
                    TextView result = (TextView) findViewById(R.id.resultText);
                    MainButton.setImageResource(R.drawable.stop);
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

    @TargetApi(Build.VERSION_CODES.M)
    public void startRec() {
        System.out.println("Start rec");
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

    public void onRecognitionDone(Recognizer recognizer, Recognition results) throws IOException{

        s = results.getBestResultText().toLowerCase();
        chat.append("\nВы: "+s);


        new PostClass(this).execute();
/*
        ChatConfig chat1 = new ChatConfig();
        chat1.instance = "11736722";
        chat1.message = s;
        ChatResponse response = connection.chat(chat1);
*/
        // ОТВЕЧАТЬ



    }

    private class PostClass extends AsyncTask<String, Void, Void> {
        private final Context context;

        public PostClass(Context c) {
            this.context = c;
        }

        @Override
        protected Void doInBackground(String... params) {
            String xml = null;
            try {
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.botlibre.com/rest/api/form-chat?instance=11736722&application=8499830136648224440").newBuilder().addQueryParameter("message", s.trim());

                String url = urlBuilder.build().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                OkHttpClient client = new OkHttpClient();
                String respXml = client.newCall(request).execute().body().string();
                Log.d("mylog", respXml.substring(respXml.indexOf("<message>")+9, respXml.indexOf("</message>")));
                bot_answer = respXml.substring(respXml.indexOf("<message>")+9, respXml.indexOf("</message>"));

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            BotTalking.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                }
            });
            chat.append("\nBot: "+bot_answer);
            createVocalizer(Vocalizer.Language.RUSSIAN,bot_answer,true,Vocalizer.Voice.JANE).start();
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
        getMenuInflater().inflate(R.menu.bot_talking, menu);
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


