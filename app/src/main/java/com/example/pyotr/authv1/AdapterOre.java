package com.example.pyotr.authv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterOre extends BaseAdapter {
    private Context mContext;
    private String[] text = {};
    private Integer[] mThumbIds = {};
    public AdapterOre(Context c,String[] text)
    {
        mContext = c;
        this.text = text;
    }

    public int getCount()
    {
        return mThumbIds.length;
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View myView = convertView;
        if (convertView == null)
        {
            LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView =li.inflate(R.layout.shift_un_angajat, null);
            TextView tv = (TextView)myView.findViewById(R.id.textViewAngajat);
            tv.setText(text[position]);
        }
        return myView;
    }
}
