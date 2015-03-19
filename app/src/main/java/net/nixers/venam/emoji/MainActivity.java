package net.nixers.venam.emoji;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.res.AssetManager;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends ActionBarActivity {
	public static List<EmojiJson> allEmotions;
	public final static String EXTRA_MESSAGE = "net.nixers.venam.emoji.MESSAGE";
    public static Vibrator mVibrator;
    public static ClipboardManager myClipboard;
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    public static CollectionPagerAdapter mCollectionPagerAdapter;
    public static ViewPager mViewPager;
    //
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    //
    private EmotionDataSource datasource;
    private ArrayAdapter<Emotion> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	MainActivity.allEmotions = new ArrayList<EmojiJson>();
    	initializeJson();
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareActionBar();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mCollectionPagerAdapter =
                new CollectionPagerAdapter(
                        getSupportFragmentManager(),this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);

        prepareDB();


        prepareDrawer();
    }

    private void prepareDB() {
        datasource = new EmotionDataSource(this);
        datasource.open();
        List<Emotion> values = datasource.getAllComments();
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        adapter = new ArrayAdapter<Emotion>(this,
                android.R.layout.simple_list_item_1, values);
        adapter.setNotifyOnChange(true);
        ListView myList=(ListView)findViewById(android.R.id.list);
        myList.setAdapter(adapter);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView arg0, View view,
                                           int position, long id) {
                Emotion emotion = (Emotion) adapter.getItem(position);
                datasource.deleteEmotion(emotion);
                adapter.remove(emotion);
                return true;
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View view,
                                    int position, long id) {
                Object o = adapter.getItem(position);
                String keyword = o.toString();
                MainActivity.mVibrator.vibrate(300);
                ClipData myClip = ClipData.newPlainText("emoji", keyword);
                MainActivity.myClipboard.setPrimaryClip(myClip);
                Toast.makeText(view.getContext(), keyword, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addEmotion(String emotion){
        Emotion comment = datasource.createEmotion(emotion);
        adapter.add(comment);
    }

    public void setActionBarTitle(String title){
        ActionBar bar = getSupportActionBar();
        bar.setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    private void prepareDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_launcher,  /* nav drawer icon to replace 'Up' caret */
                R.string.hello,  /* "open drawer" description */
                R.string.hello  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Emoji");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Favorite Emojis");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void prepareActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Emoji");
        bar.show();
    }
    
    private void initializeJson() {
    	InputStream mInput = null;
    	AssetManager manager = getAssets();
        try {
			mInput = manager.open("emoji.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String theString = convertStreamToString(mInput);
        JsonElement json = new JsonParser().parse(theString);
        JsonArray array= json.getAsJsonArray();

        //fill up the array of allEmotions
        Iterator<JsonElement> iterator = array.iterator();
        while(iterator.hasNext()){
            JsonElement json2 = (JsonElement)iterator.next();
            Gson gson = new Gson();
            EmojiJson emo = gson.fromJson(json2, EmojiJson.class); 
            MainActivity.allEmotions.add(emo);
        }
    }
    
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        MainActivity.mViewPager.setCurrentItem(0);
        return;
    }

}
