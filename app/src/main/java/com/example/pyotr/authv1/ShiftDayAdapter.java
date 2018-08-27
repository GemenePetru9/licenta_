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
         int culoare=mEmployeeData.get(position).getCuloare();

            if(ziua!=null) {

                switch (ziua) {
                    case "Monday":
                        holder.sEmployeeShift.setText(date.get("Monday"));
                        checkIfIsDayOff(holder.sEmployeeShift,date.get("Monday"),culoare);
                        break;
                    case "Tuesday":
                        holder.sEmployeeShift.setText(date.get("Tuesday"));
                        checkIfIsDayOff(holder.sEmployeeShift,date.get("Tuesday"),culoare);
                        break;
                    case "Wednesday":
                        holder.sEmployeeShift.setText(date.get("Wednesday"));
                        checkIfIsDayOff(holder.sEmployeeShift,date.get("Wednesday"),culoare);
                        break;
                    case "Thusday":
                        holder.sEmployeeShift.setText(date.get("Thusday"));
                        checkIfIsDayOff(holder.sEmployeeShift,date.get("Thusday"),culoare);
                        break;
                    case "Friday":
                        holder.sEmployeeShift.setText(date.get("Friday"));
                        checkIfIsDayOff(holder.sEmployeeShift,date.get("Friday"),culoare);
                        break;
                    case "Saturday":
                        holder.sEmployeeShift.setText(date.get("Saturday"));
                        checkIfIsDayOff(holder.sEmployeeShift,date.get("Saturday"),culoare);
                        break;
                    case "Sunday":
                        holder.sEmployeeShift.setText(date.get("Sunday"));
                        checkIfIsDayOff(holder.sEmployeeShift,date.get("Sunday"),culoare);
                        break;
                }
            }

            //holder.sFlowerPhotoPath.setImageResource(mFlowerData.get(position).getPhotoPath());
        }

        return convertView;
    }
    private void checkIfIsDayOff(TextView textView,String shift,int culoare)
    {
        if(!shift.equals("off"))
        {
            textView.setBackgroundColor(culoare);
        }
        else
        {

        }
    }

    private static class ViewHolder {
        TextView sEmployeeName;
        TextView sEmployeeShift;
    }


}
