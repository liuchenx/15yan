package org.liuyichen.fifteenyan.ui.adapter;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

/**
 * By liuyichen on 14-12-2 下午4:49.
 */
@SuppressWarnings("unused")
public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private boolean mDataValid;

    private boolean mAutoRequery;

    private Cursor mCursor;

    Context mContext;

    private int mRowIDColumn;

    private ChangeObserver mChangeObserver;

    private DataSetObserver mDataSetObserver;

    @Deprecated
    private static final int FLAG_AUTO_REQUERY = 0x01;

    private static final int FLAG_REGISTER_CONTENT_OBSERVER = 0x02;

    @Deprecated
    CursorAdapter(Context context, @SuppressWarnings("SameParameterValue") Cursor c) {
        //noinspection deprecation
        init(context, c, FLAG_AUTO_REQUERY);
    }

    CursorAdapter(Context context, Cursor c, boolean autoRequery) {
        //noinspection deprecation
        init(context, c, autoRequery ? FLAG_AUTO_REQUERY : FLAG_REGISTER_CONTENT_OBSERVER);
    }

    CursorAdapter(Context context, Cursor c, int flags) {
        init(context, c, flags);
    }

    private void init(Context context, Cursor c, int flags) {

        //noinspection deprecation,deprecation
        if ((flags & FLAG_AUTO_REQUERY) == FLAG_AUTO_REQUERY) {
            flags |= FLAG_REGISTER_CONTENT_OBSERVER;
            mAutoRequery = true;
        } else {
            mAutoRequery = false;
        }
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mContext = context;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        if ((flags & FLAG_REGISTER_CONTENT_OBSERVER) == FLAG_REGISTER_CONTENT_OBSERVER) {
            mChangeObserver = new ChangeObserver();
            mDataSetObserver = new MyDataSetObserver();
        } else {
            mChangeObserver = null;
            mDataSetObserver = null;
        }

        if (cursorPresent) {
            if (mChangeObserver != null) {
                c.registerContentObserver(mChangeObserver);
            }
            if (mDataSetObserver != null) {
                c.registerDataSetObserver(mDataSetObserver);
            }
        }
    }

    protected abstract void onBindViewHolder(VH holder, Cursor cursor, int position);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (!mDataValid) {
            onBindViewHolder(holder, null, position);
            return;
        //    throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            onBindViewHolder(holder, null, position);
            return;
            //throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, mCursor, position);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIDColumn);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    private void onContentChanged() {
        if (mAutoRequery && mCursor != null && !mCursor.isClosed()) {
            //noinspection deprecation
            mDataValid = mCursor.requery();
        }
    }

    private class ChangeObserver extends ContentObserver {

        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid = false;
            notifyDataSetChanged();
        }
    }
}
