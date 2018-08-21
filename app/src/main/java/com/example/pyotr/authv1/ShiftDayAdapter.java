package com.example.pyotr.authv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShiftDayAdapter extends BaseAdapter {

    private ArrayList<Client> mEmployeeData = new ArrayList<>();
    private LayoutInflater mInflaterCatalogListItems;
    private String ziua="";


    public ShiftDayAdapter(Context context, ArrayList<Client> employeeData,String day) {
        mEmployeeData = employeeData;
        mInflaterCatalogListItems = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ziua=day;
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

        ShiftDayAdapter.ViewHolder holder;

        if (convertView == null) {

            holder = new ShiftDayAdapter.ViewHolder();
            convertView = mInflaterCatalogListItems.inflate(R.layout.shift_day_cell,
                    null);
            holder.sEmployeeName = (TextView) convertView.findViewById(R.id.textViewNumeAngajat);
            holder.sEmployeeShift = (TextView) convertView.findViewById(R.id.textViewShiftAngajat);



            //holder.sFlowerPhotoPath = (ImageView) convertView.findViewById(R.id.photoView);
            convertView.setTag(holder);

        } else {
            holder = (ShiftDayAdapter.ViewHolder) convertView.getTag();
        }


        //Change the content here
        if (mEmployeeData.get(position) != null) {
            holder.sEmployeeName.setText(mEmployeeData.get(position).getNume());


            Map<String,String> date=mEmployeeData.get(position).getDay();
            System.out.println("DayAdapter:"+date.size());
            System.out.println("DayAdapter:"+date.get("Monday"));
            if(ziua!=null) {

                switch (ziua) {
                    case "Monday":
                        holder.sEmployeeShift.setText(date.get("Monday"));
                        break;
                    case "Tuesday":
                        holder.sEmployeeShift.setText(date.get("Tuesday"));
                        break;
                    case "Wednesday":
                        holder.sEmployeeShift.setText(date.get("Wednesday"));
                        break;
                    case "Thusday":
                        holder.sEmployeeShift.setText(date.get("Thusday"));
                        break;
                    case "Friday":
                        holder.sEmployeeShift.setText(date.get("Friday"));
                        break;
                    case "Saturday":
                        holder.sEmployeeShift.setText(date.get("Saturday"));
                        break;
                    case "Sunday":
                        holder.sEmployeeShift.setText(date.get("Sunday"));
                        break;
                }
            }

            //holder.sFlowerPhotoPath.setImageResource(mFlowerData.get(position).getPhotoPath());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView sEmployeeName;
        TextView sEmployeeShift;
    }


}
