package com.development.forty_two.myapplication;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class DictionaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        SearchView search = (SearchView) findViewById(R.id.searchWord);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showResults(newText);
                return false;
            }
        });

    }

    private void showResults(String query) {
        if(query.length() >= 3){
            //TODO выполнить запрос к базе за словами
            ArrayList<String> words = new ArrayList<>();
            words.add("Word 1");
            words.add("Word 2");
            ListView list = (ListView) findViewById(R.id.listWords);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.list_item,words);

            list.setAdapter(adapter);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        this.closeOptionsMenu();
        int id = item.getItemId();
        Intent i;
        switch (id){
            case R.id.menu_main:
                i = new Intent(DictionaryActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_settings:
                i = new Intent(DictionaryActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
        }

        //noinspection SimplifiableIfStatement


        return true;
    }
}
