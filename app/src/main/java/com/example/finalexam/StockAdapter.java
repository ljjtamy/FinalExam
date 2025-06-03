package com.example.finalexam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockAdapter extends ArrayAdapter<HashMap<String, String>> {

    private int resourceId;

    public StockAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, String> stock = getItem(position);
        ViewHolder viewHolder;
    
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.stockName = convertView.findViewById(R.id.stockName);
            viewHolder.currentPrice = convertView.findViewById(R.id.currentPrice);
            viewHolder.changePercent = convertView.findViewById(R.id.changePercent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
    
        // 设置数据
        viewHolder.stockName.setText(stock.get("stockName"));
        viewHolder.currentPrice.setText(stock.get("currentPrice"));
        viewHolder.changePercent.setText(stock.get("changePercent"));
    
        // 设置颜色
        double changePercent = Double.parseDouble(stock.get("changePercent").replace("%", ""));
        int color = changePercent > 0 ? android.R.color.holo_red_dark : 
                  changePercent < 0 ? android.R.color.holo_green_dark : 
                  android.R.color.primary_text_dark;
        viewHolder.currentPrice.setTextColor(getContext().getResources().getColor(color));
        viewHolder.changePercent.setTextColor(getContext().getResources().getColor(color));
    
        return convertView;
    }

    class ViewHolder {
        TextView stockName;
        TextView currentPrice;
        TextView changePercent;
    }
}