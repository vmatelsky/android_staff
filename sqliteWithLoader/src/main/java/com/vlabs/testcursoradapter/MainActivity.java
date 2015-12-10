package com.vlabs.testcursoradapter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ENTITIES_LOADER_ID = 1;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.entry_list);
        getSupportLoaderManager().initLoader(ENTITIES_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {

        if (id == ENTITIES_LOADER_ID) {
            return new SimpleCursorLoader(this) {
                @Override
                public Cursor loadInBackground() {
                    return new DbHelper(MainActivity.this).fetchAllData();
                }
            };
        }
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        listView.setAdapter(new EntityCursorAdapter(this, cursor));
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
    }
}
