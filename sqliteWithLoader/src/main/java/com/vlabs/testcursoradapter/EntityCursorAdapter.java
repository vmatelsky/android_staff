package com.vlabs.testcursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class EntityCursorAdapter extends CursorAdapter {

    public EntityCursorAdapter(final Context context, final Cursor cursor) {
        // The last argument is 0 because there is a problem with constructor without it
        // The problem is that it will have the SimpleCursorAdapter auto-requery
        // its data when changes are made.
        //
        // More specifically, the CursorAdapter will register a ContentObserver
        // that monitors the underlying data source for changes,
        // calling requery() on its bound cursor each time the data is modified.
        // The standard constructor should be used instead
        // (if you intend on loading the adapter's data with a CursorLoader,
        // make sure you pass 0 as the last argument).
        // Don't worry if you couldn't spot this one... it's a very subtle bug.
        //
        // http://www.androiddesignpatterns.com/2012/07/loaders-and-loadermanager-background.html
        super(context, cursor, 0);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_example_entry, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ImageView thumbnailView = (ImageView) view.findViewById(R.id.thimbnail);
        final TextView titleView = (TextView) view.findViewById(R.id.title);
        final TextView subtitleView = (TextView) view.findViewById(R.id.subtitle);

        final String title = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
        final String subtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));
        final String thumbnailUrl = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.IMAGE_URL));

        titleView.setText(title);
        subtitleView.setText(subtitle);

        Glide
                .with(context)
                .load(thumbnailUrl)
                .into(thumbnailView);
    }
}
