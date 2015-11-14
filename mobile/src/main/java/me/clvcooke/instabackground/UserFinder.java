package me.clvcooke.instabackground;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import me.clvcooke.instabackground.Adapters.ImageGridAdapter;
import me.clvcooke.instabackground.Utilities.UtilityMethods;


public class UserFinder extends Activity {

    private final String INSTAGRAM_URL_PREFIX = "https://instagram.com/";
    private ImageGridAdapter imageGridAdapter;
    private Activity activity;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);
        activity = this;


        Button searchButton = (Button) findViewById(R.id.search_button);
        Button saveButton = (Button) findViewById(R.id.save_button);

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

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(activity,
                        "android.permission.WRITE_EXTERNAL_STORAGE")
                        != PackageManager.PERMISSION_GRANTED) {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity,
                            new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                            0);
                } else {
                    imageGridAdapter.saveSelected();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Runnable loadPhotos = (new Runnable() {
                    @Override
                    public void run() {
                        String username = textField.getText().toString();
                        final Context context = view.getContext();

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

                        ArrayList<String> urls;
                        try {
                            urls = UtilityMethods.getURLS(INSTAGRAM_URL_PREFIX + username, 23);
                        } catch (IOException e) {
                            makeToast("Unable to load Instagram", context);
                            e.printStackTrace();
                            return;
                        }


                        //make a hashmap of the last 28 chars of the url
                        HashMap<String, Integer> urlEndings = new HashMap<>();

                        for (int i = 0; i < urls.size(); i++) {
                            String url = urls.get(i);
                            urlEndings.put(url.substring(url.length() - 32), i);
                        }


                        HashSet<Integer> urlsDownloaded = new HashSet<>();
                        //TODO make this 2n
                        File[] files = UtilityMethods.getSavedFiles(UtilityMethods.DIRECTORY_PREFIX + username);
                        if (files != null) {
                            for (File file : files) {
                                String fileName = file.getName();
                                String substring = fileName.substring(fileName.length() - 32);
                                if (urlEndings.containsKey(substring)) {
                                    urlsDownloaded.add(urlEndings.get(substring));
                                }
                            }
                        }

                        imageGridAdapter.loadUrls(urlsDownloaded, urls, username);


                        if (urls.size() == 0) {
                            makeToast("No images found", context);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_picker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
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
