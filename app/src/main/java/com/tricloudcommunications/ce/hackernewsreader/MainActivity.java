package com.tricloudcommunications.ce.hackernewsreader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView newsLV;
    ArrayList<String> newsList;
    ArrayAdapter arrayAdapter;

    SQLiteDatabase newsFeedDB;

    /**
     * Check if the database exist and can be read.
     *
     * @return true if it exists and can be read, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(String.valueOf(this.getDatabasePath("HackNews")), null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {

            e.printStackTrace();
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    public void getDBData(){

        try {

            newsFeedDB = this.openOrCreateDatabase("HackNews", MODE_PRIVATE, null);

            Cursor c = newsFeedDB.rawQuery("SELECT * FROM news", null);

            int IdIndex = c.getColumnIndex("id");
            int titleIndex = c.getColumnIndex("title");
            int urlIndex = c.getColumnIndex("url");
            int timeIndex = c.getColumnIndex("time");

            c.moveToFirst();

            while (c != null) {

                Log.i("Database Data:", "ID: " + c.getString(IdIndex) + " Title: " + c.getString(titleIndex) + " URL: " + c.getString(urlIndex) + " Time: " + c.getString(timeIndex));

                c.moveToNext();
            }
        }catch (Exception e){

            e.printStackTrace();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newsLV = (ListView) findViewById(R.id.newsListView);

        newsList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, newsList);
        newsLV.setAdapter(arrayAdapter);


        if (checkDataBase()){

            //If we are here than the Database exists and we want to add some data from the Hacker News API
            //getDBData();

            try {

                newsFeedDB = this.openOrCreateDatabase("HackNews", MODE_PRIVATE, null);

                Cursor c = newsFeedDB.rawQuery("SELECT * FROM news", null);

                int IdIndex = c.getColumnIndex("id");
                int titleIndex = c.getColumnIndex("title");
                int urlIndex = c.getColumnIndex("url");
                int timeIndex = c.getColumnIndex("time");

                c.moveToFirst();

                while (c != null) {

                    newsList.add(c.getString(titleIndex));

                    Log.i("Database Data:", "ID: " + c.getString(IdIndex) + " Title: " + c.getString(titleIndex) + " URL: " + c.getString(urlIndex) + " Time: " + c.getString(timeIndex));

                    c.moveToNext();
                }
            }catch (Exception e){

                e.printStackTrace();

            }



            //Will return true if database exists and can be read
            Log.i("Database Status", Boolean.toString(checkDataBase()) + " The IF condition");

        }else{

            //If we are here then that means the database does not exist and we need to create it, and create the table and the field/colums we need
            newsFeedDB = this.openOrCreateDatabase("HackNews", MODE_PRIVATE, null);
            newsFeedDB.execSQL("CREATE TABLE IF NOT EXISTS news (id INTEGER PRIMARY KEY, title VARCHAR, url VARCHAR, time VARCHAR)");
            //Add a dummy data
            newsFeedDB.execSQL("INSERT INTO news (title, url, time) VALUES('The next Big Thing TricciMe', 'http://tricloudcommunications.com/login.php', '12192016')");

            //Read the database to see if data was inserted and print to log
            getDBData();

            //Will return false if database DOES NOT exists and it can NOT be read
            Log.i("Database Status", Boolean.toString(checkDataBase()) + " The Else Condition");
        }



        //Log.i("Database Status", Boolean.toString(checkDataBase()));
        //Log.i("Database Path:", String.valueOf(this.getDatabasePath("HackNews")));
        //data/data/com.tricloudcommunications.ce.hackernewsreader/databases/HackNews
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
