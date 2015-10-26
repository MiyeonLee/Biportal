package br.liveo.ndrawer.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.liveo.ndrawer.R;

/**
 * Created by josephine.lee on 2015-10-21.
 */
public class DeviceListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<BeaconDevice> mDeviceList = new ArrayList<BeaconDevice>();
    int layout;

    public DeviceListAdapter(Context context, int iLayout){
        mContext = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = iLayout;
    }

    public void setDeviceList(ArrayList<BeaconDevice> newList){
        mDeviceList = newList;

        String msg = "setDeviceList=" + mDeviceList.size();
        Log.d("BLE", msg);
    }

    public int getCount(){
        return mDeviceList.size();
    }

    public Object getItem(int position){
        return mDeviceList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }

        // TODO: list item setting
        TextView name = (TextView)convertView.findViewById(R.id.deviceName);
        name.setText(mDeviceList.get(position).getBdName());

        TextView address = (TextView)convertView.findViewById(R.id.deviceID);
        address.setText(mDeviceList.get(position).getBdAddr());

        TextView strength = (TextView)convertView.findViewById(R.id.rssi);
        // TODO: Distance 계산 필요
        double accuracy = calculateAccuracy(mDeviceList.get(position).getTxPower(), mDeviceList.get(position).getRssi());
        if(accuracy < 0)
            strength.setText("Unknown");
        else if(accuracy < 0.5) // under 0.5m
            strength.setText("Immediate="+accuracy+"m");
        else if(accuracy < 4.0) // under 4.0m
            strength.setText("Near="+accuracy+"m");
        else
            strength.setText("Far="+accuracy+"m");

        return convertView;
    }

    // parameters based on Nexus4
    private double calculateAccuracy(int txPower, int rssi){
        double accuracy = 0;
        if(rssi == 0){
            return -1.0;
        }
        double ratio = rssi*1.0/txPower;
        if(ratio < 1.0){
            return Math.pow(ratio, 10);
        }
        else{
            accuracy = (0.89976)*Math.pow(ratio, 7.7095) + 0.111;
        }
        return accuracy;
    }
}
