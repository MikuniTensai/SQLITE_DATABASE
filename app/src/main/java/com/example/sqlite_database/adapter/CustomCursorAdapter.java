package com.example.sqlite_database.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.sqlite_database.helper.DBHelper_Account;
import com.example.sqlite_database.R;

public class CustomCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.row_data, viewGroup, false);
        MyHolder holder = new MyHolder();
        holder.ListID = v.findViewById(R.id.listID);
        holder.ListNama = v.findViewById(R.id.listNama);
        holder.ListSelengkapnya = v.findViewById(R.id.listSelengkapnya);
        holder.ListFoto = v.findViewById(R.id.listFoto);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyHolder holder = (MyHolder)view.getTag();
        holder.ListID.setText(cursor.getString(cursor.getColumnIndex(DBHelper_Account.row_id)));
        holder.ListNama.setText(cursor.getString(cursor.getColumnIndex(DBHelper_Account.row_nama)));
        holder.ListSelengkapnya.setText(cursor.getString(cursor.getColumnIndex(DBHelper_Account.row_selengkapnya)));
        Glide.with(holder.ListFoto.getContext())
                .load(cursor.getString(cursor.getColumnIndex(DBHelper_Account.row_foto)))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .apply(new RequestOptions().transform(new RoundedCorners(50)).error(R.drawable.ic_add).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(holder.ListFoto);
    }

    class MyHolder{
        TextView ListID;
        TextView ListNama;
        TextView ListSelengkapnya;
        ImageView ListFoto;
    }
}
