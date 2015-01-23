package me.clvcooke.instabackground;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    //TODO load from file
    private int maxPics = 19;
    private EditText usernameTextView;
    private EditText numberText;
    private SeekBar imageBar;
    private SeekBar timeBar;
    private TextView pictureCounter;
    private ImageView imageView;
    private Bitmap badPicture;

    private final String INSTAGRAM_URL_PREFIX = "http://instagram.com/";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO don't do this
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        badPicture = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);

        Button button = (Button) findViewById(R.id.search_button);
        imageBar = (SeekBar) findViewById(R.id.seekBar);
        imageBar.setMax(maxPics);
        pictureCounter = (TextView) findViewById(R.id.pictureCounter);
        pictureCounter.setText(Integer.toString(1));
        usernameTextView = (EditText) findViewById(R.id.username);
        numberText = (EditText) findViewById(R.id.timeText);
        imageView = (ImageView) findViewById(R.id.imageView);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextView.getText().toString();
                Context context = view.getContext();
                int pictureNum;

                if(username.isEmpty()) {
                    Toast.makeText(context, "Invalid - Empty name", Toast.LENGTH_SHORT).show();
                    return;
                }else if(username.contains(" ")){
                    username = username.replaceAll("\\s", ""); //replacing all spaces in the string
                }
                try {
                     pictureNum = Integer.parseInt(pictureCounter.getText().toString());
                }catch (NumberFormatException e){
                    Toast.makeText(context, "Invalid - Bad picture amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                String title;

                try{
                    title = UtilityMethods.getPageTitle(INSTAGRAM_URL_PREFIX + username);
                }catch (IOException e) {
                    Toast.makeText(context, "Unable to Load Instagram User", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO find better way to check if user exists
                if(!title.contains("Page Not Found")){
                 Toast.makeText(context, "Valid user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Invalid user", Toast.LENGTH_SHORT).show();
                    return;
                }




                List<String> urls;
                try {
                    urls = UtilityMethods.getURLS(INSTAGRAM_URL_PREFIX + username,pictureNum);
                }catch(IOException e){
                    Toast.makeText(context, "Unable to Load Instagram", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }

                new LoadPictureTask(context).execute(urls);
            }
        });

        imageBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pictureCounter.setText(Integer.toString(progress + 1/* +1 for no zero picture amount*/));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class LoadPictureTask extends AsyncTask<List<String>, Void, List<Bitmap>> {

        private Context m_context;

        public LoadPictureTask(Context context){
            m_context = context;
        }

        @Override
        protected List<Bitmap> doInBackground(List<String>... urls) {

            List<Bitmap>bitmaps = new ArrayList<Bitmap>();

            for(int i = 0; i < urls[0].size(); i++){
                Bitmap bitmap;
                try{
                    bitmap = Picasso.with(m_context).load(urls[0].get(i)).get();
                }catch (IOException e){
                    bitmap = badPicture;
                }
                bitmaps.add(bitmap);
            }


            return bitmaps;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmaps){
            imageView.setImageBitmap(bitmaps.get(0));

            UtilityMethods.setWallpaper(m_context, bitmaps.get(0));

            for(int i = 0; i < bitmaps.size(); i++){
                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

                File directory = contextWrapper.getDir(UtilityMethods.IMAGE_DIR, Context.MODE_PRIVATE);

                File path = new File(directory, UtilityMethods.IMAGE_FILE_NAME_PREFIX + Integer.toString(i+1));

                FileOutputStream fos = null;

                try{
                    fos = new FileOutputStream(path);

                    bitmaps.get(i).compress(Bitmap.CompressFormat.PNG,100,fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }


            String timeText = numberText.getText().toString();

            int timeInSeconds;

            if(timeText.isEmpty()){
                timeInSeconds = 60;
            }else{
                timeInSeconds = Integer.parseInt(timeText);
            }

            AlarmReceiver alarmReceiver = new AlarmReceiver();

            alarmReceiver.setAlarm(timeInSeconds,m_context,bitmaps.size());

            Toast.makeText(m_context,"alarm set", Toast.LENGTH_SHORT).show();

        }
    }





}
