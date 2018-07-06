package com.events.shirow.eventstalker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.SearchableProvider;

public class SearchableActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("SEARCHING");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        hendleSearch( getIntent() );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
    }


    public void hendleSearch( Intent intent ){
        if( Intent.ACTION_SEARCH.equalsIgnoreCase( intent.getAction() ) ){
            String q = intent.getStringExtra( SearchManager.QUERY );

            mToolbar.setTitle(q);
            Toast.makeText(SearchableActivity.this, "Handling Search", Toast.LENGTH_SHORT).show();

            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery( q, null );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_searchable, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_search);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( item );
        }

        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == R.id.action_delete ){
            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);

            searchRecentSuggestions.clearHistory();

            Toast.makeText(this, "History Cleared", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
