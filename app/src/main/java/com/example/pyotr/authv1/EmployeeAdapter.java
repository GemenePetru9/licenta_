package com.example.pyotr.authv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeAdapter extends BaseAdapter {

    private ArrayList<Client> mEmployeeData = new ArrayList<>();
    private LayoutInflater mInflaterCatalogListItems;


    public EmployeeAdapter(Context context, ArrayList<Client> employeeData) {
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

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflaterCatalogListItems.inflate(R.layout.shift_un_angajat,
                    null);
            holder.sEmployeeName = (TextView) convertView.findViewById(R.id.textViewAngajat);



            //holder.sFlowerPhotoPath = (ImageView) convertView.findViewById(R.id.photoView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //Change the content here
        if (mEmployeeData.get(position) != null) {
            holder.sEmployeeName.setText(mEmployeeData.get(position).getNume());
            System.out.println("Dateeeeeeeeee:"+mEmployeeData.get(position).getNume());
            //holder.sFlowerPhotoPath.setImageResource(mFlowerData.get(position).getPhotoPath());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView sEmployeeName;

        //ImageView sFlowerPhotoPath;

    }
}
