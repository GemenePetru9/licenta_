package com.example.pyotr.authv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayAdapter extends BaseAdapter {

    private ArrayList<Client> mEmployeeData = new ArrayList<>();
    private LayoutInflater mInflaterCatalogListItems;


    public DayAdapter(Context context, ArrayList<Client> employeeData) {
        mEmployeeData = employeeData;
        mInflaterCatalogListItems = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public int getCount() {
        return mEmployeeData.size() ;
    }

    @Override
    public Object getItem(int position) {
        return mEmployeeData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        int cellHeight = 50;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflaterCatalogListItems.inflate(R.layout.shift_day_cell,
                    null);
            holder.sDay1 = (TextView) convertView.findViewById(R.id.data1);
            holder.sDay2 = (TextView) convertView.findViewById(R.id.data2);
            holder.sDay3 = (TextView) convertView.findViewById(R.id.data3);
            holder.sDay4 = (TextView) convertView.findViewById(R.id.data4);
            holder.sDay5 = (TextView) convertView.findViewById(R.id.data5);
            holder.sDay6 = (TextView) convertView.findViewById(R.id.data6);
            holder.sDay7 = (TextView) convertView.findViewById(R.id.data7);



            //holder.sFlowerPhotoPath = (ImageView) convertView.findViewById(R.id.photoView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //Change the content here
        if (mEmployeeData.get(position) != null && mEmployeeData.get(position).getDay()!=null) {
            List<String> date1=mEmployeeData.get(position).getDay();

            System.out.println("DayAdapter:"+date1.size());
            System.out.println("DayAdapter:"+date1.get(0));

           holder.sDay1.setText(date1.get(0));

            holder.sDay1.setText(date1.get(0));
            holder.sDay2.setText(date1.get(1));
            holder.sDay3.setText(date1.get(2));
            holder.sDay4.setText(date1.get(3));
            holder.sDay5.setText(date1.get(4));
            holder.sDay6.setText(date1.get(5));
            holder.sDay7.setText(date1.get(6));
            //aici preluam datele
          // System.out.println("Date din DayAdapter:"+date1[6]);
            //holder.sFlowerPhotoPath.setImageResource(mFlowerData.get(position).getPhotoPath());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView sDay1;
        TextView sDay2;
        TextView sDay3;
        TextView sDay4;
        TextView sDay5;
        TextView sDay6;
        TextView sDay7;

        //ImageView sFlowerPhotoPath;

    }
}
