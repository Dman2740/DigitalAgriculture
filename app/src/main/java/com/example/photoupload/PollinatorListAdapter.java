package com.example.photoupload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PollinatorListAdapter extends ArrayAdapter<Pollinator>
{
    private static final String TAG="PollinatorListAdapter";
    private Context mcontext;
    private int mresources;
    private static class ViewHolder
    {
        ImageView image;
        TextView id;
        TextView pollinatorName;
        TextView name;
        TextView date;
    }

    public PollinatorListAdapter(Context context,int resource,ArrayList<Pollinator> objects)
    {
        super(context,resource,objects);
        mcontext=context;
        mresources=resource;
    }
    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        byte[] image=getItem(position).getImage();
        String pollinatorName=getItem(position).getPollName();
        String name=getItem(position).getName();
        String date=getItem(position).getDate();

        Pollinator polly=new Pollinator(image);
        Pollinator pollinator=new Pollinator(pollinatorName,name,date);

        final View result;

        ViewHolder holder;

        if(convertView==null)
        {
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mresources, parent, false);
            holder= new ViewHolder();
            holder.pollinatorName = (TextView) convertView.findViewById(R.id.textPollinator);
            holder.name = (TextView) convertView.findViewById(R.id.textName);
            holder.date= (TextView) convertView.findViewById(R.id.textDate);
            holder.image=(ImageView) convertView.findViewById(R.id.imagey);

            result = convertView;

            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
            result=convertView;
        }
        return convertView;

    }

}
