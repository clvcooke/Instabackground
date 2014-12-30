package me.clvcooke.instabackground;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    //TODO load from file
    private int maxPics = 24;
    private EditText usernameTextView;
    private SeekBar bar;
    private TextView pictureCounter;

    private final String INSTAGRAM_URL_PREFIX = "http://instagram.com/";
    private final String RSS_URL_PREFIX = "http://widget.websta.me/rss/n/";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //TODO don't do this
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Button button = (Button) findViewById(R.id.search_button);
        bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setMax(maxPics);
        pictureCounter = (TextView) findViewById(R.id.pictureCounter);
        pictureCounter.setText(Integer.toString(1));
        usernameTextView = (EditText) findViewById(R.id.username);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextView.getText().toString();
                int pictureNum;
                if(username.isEmpty()) {
                    Toast.makeText(view.getContext(), "Invalid - Empty name", Toast.LENGTH_SHORT).show();
                    return;
                }else if(username.contains(" ")){
                    username = username.replaceAll("\\s", ""); //replacing all spaces in the string
                }
                try {
                     pictureNum = Integer.parseInt(pictureCounter.getText().toString());
                }catch (NumberFormatException e){
                    Toast.makeText(view.getContext(), "Invalid - Bad picture amount", Toast.LENGTH_SHORT).show();
                }

                String title;



                try{
                    title = UtilityMethods.getPageTitle(INSTAGRAM_URL_PREFIX + username);
                }catch (IOException e) {
                    Toast.makeText(view.getContext(), "Unable to Load Instagram", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO find better way to check if user exists
                if(title.contains(username)){
                 Toast.makeText(view.getContext(), "Valid user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(view.getContext(), "Invalid user", Toast.LENGTH_SHORT).show();
                    return;
                }

                String rssURL = RSS_URL_PREFIX + username;
                List<String> urls;
                try {
                    urls = UtilityMethods.getImageURLSFromRSS(rssURL, 10);
                }catch(IOException e){
                    Toast.makeText(view.getContext(), "Unable to Load Instagram", Toast.LENGTH_SHORT).show();
                    return;
                }





            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pictureCounter.setText(Integer.toString(progress+1/* +1 for no zero picture amount*/));
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

}
