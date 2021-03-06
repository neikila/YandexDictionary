package com.development.forty_two.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import dbservice.Translate;
import dictionary.Dictionary;

public class DictionaryActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dictionary = Dictionary.getInstance();

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


    @Override
    protected void onStart() {
        super.onStart();
        bus = ((ApplicationModified)getApplication()).getBus();
        bus.register(this);
    }

    private void showResults(String query) {
        if(query.length() >= 3){
            new GetTranslationAsyncTask().execute(query);
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.unregister(this);
    }

    private class ListItemTranslationAdapter extends ArrayAdapter<Translate> {
        public ListItemTranslationAdapter(List<Translate> objects) {
            super(DictionaryActivity.this, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.translate = (TextView) convertView.findViewById(R.id.item_translate);
                viewHolder.lang = (TextView) convertView.findViewById(R.id.item_lang);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.translate.setText(getItem(position).getTranslate());
            viewHolder.lang.setText(getItem(position).getLanguage());
            return convertView;
        }

        private class ViewHolder {
            public TextView translate;
            public TextView lang;
        }
    }

    private class GetTranslationAsyncTask extends AsyncTask <String, Void, Void> {
        List <Translate> result;

        @Override
        protected Void doInBackground(String... params) {
            result = dictionary.getTranslations(params[0]);
            if (result == null) {
                result = new ArrayList<>();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListItemTranslationAdapter adapter = new ListItemTranslationAdapter(result);
            ListView list = (ListView) findViewById(R.id.listWords);
            list.setAdapter(adapter);
        }
    }
}