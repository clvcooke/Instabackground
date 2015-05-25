package me.clvcooke.instabackground;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.clvcooke.instabackground.Adapters.ImageGridAdapter;


public class UserFinder extends Activity {

    private EditText usernameTextView;
    private final String INSTAGRAM_URL_PREFIX = "https://instagram.com/";
    private ImageGridAdapter imageGridAdapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);


        Button searchButton = (Button) findViewById(R.id.search_button);
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button setButton = (Button) findViewById(R.id.background_button);

        final EditText textField = (EditText) findViewById(R.id.username);
        final GridView gridView = (GridView) findViewById(R.id.gridView);

        imageGridAdapter = new ImageGridAdapter(this);
        gridView.setAdapter(imageGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ImageGridAdapter) gridView.getAdapter()).onItemClick(view, position);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((ImageGridAdapter) gridView.getAdapter()).onItemLongClick(position);
                return true;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGridAdapter.saveSelected();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGridAdapter.saveSelected();
                //save photos and transition to background settings page
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Runnable loadPhotos = (new Runnable() {
                    @Override
                    public void run() {
                        String username = textField.getText().toString();
                        Context context = view.getContext();

                        if (username.isEmpty()) {
                            makeToast("Invalid - Empty name", context);
                            return;
                        } else if (username.contains(" ")) {
                            username = username.replaceAll("\\s", ""); //replacing all spaces in the string
                        }

                        String title;

                        try {
                            title = UtilityMethods.getPageTitle(INSTAGRAM_URL_PREFIX + username + "/");
                        } catch (IOException e) {
                            makeToast("Instagram cannot be reached", context);
                            return;
                        }

                        //TODO find better way to check if user exists
                        if (!title.contains("Page Not Found")) {
                            makeToast("Valid user", context);
                        } else {
                            makeToast("Invalid User", context);
                            return;
                        }

                        List<String> urls;
                        try {
                            urls = UtilityMethods.getURLS(INSTAGRAM_URL_PREFIX + username, 20);
                        } catch (IOException e) {
                            makeToast("Unable to load Instagram", context);
                            e.printStackTrace();
                            return;
                        }


                        imageGridAdapter.setUrls(urls, username);
                        File[] files = UtilityMethods.getSavedFiles(UtilityMethods.DIRECTORY_PREFIX + username);
                        if (files != null) {
                            for (File file : files) {
                                //TODO this will need to be updated when we can get more urls at once
                                int i = 0;
                                for (String url : urls) {
                                    if (url.contains(file.getName())) {
                                        urls.remove(i);
                                        break;
                                    }
                                    i++;
                                }
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageGridAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

                new Thread(loadPhotos).start();
            }
        });

    }


    public void makeToast(final String messsage, final Context context) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, messsage, Toast.LENGTH_SHORT).show();
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

        public LoadPictureTask(Context context) {
            m_context = context;
        }

        @Override
        protected List<Bitmap> doInBackground(List<String>... urls) {

            List<Bitmap> bitmaps = new ArrayList<Bitmap>();


            return bitmaps;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmaps) {


            UtilityMethods.setWallpaper(m_context, bitmaps.get(0));

            for (int i = 0; i < bitmaps.size(); i++) {
                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

                File directory = contextWrapper.getDir(UtilityMethods.IMAGE_DIR, Context.MODE_PRIVATE);

                File path = new File(directory, UtilityMethods.IMAGE_FILE_NAME_PREFIX + Integer.toString(i + 1));

                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(path);

                    bitmaps.get(i).compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }


            AlarmReceiver alarmReceiver = new AlarmReceiver();

            // alarmReceiver.setAlarm(timeInSeconds,m_context,bitmaps.size());

            Toast.makeText(m_context, "alarm set", Toast.LENGTH_SHORT).show();

        }
    }


}
