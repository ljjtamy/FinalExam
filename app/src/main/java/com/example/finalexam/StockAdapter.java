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
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.stockName = view.findViewById(R.id.stockName);
            viewHolder.currentPrice = view.findViewById(R.id.currentPrice);
            viewHolder.changePercent = view.findViewById(R.id.changePercent);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.stockName.setText(stock.get("stockName"));
        viewHolder.currentPrice.setText(stock.get("currentPrice"));
        viewHolder.changePercent.setText(stock.get("changePercent"));

        // 根据涨跌设置颜色
        double changePercent = Double.parseDouble(stock.get("changePercent").replace("%", ""));
        if (changePercent > 0) {
            viewHolder.currentPrice.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
            viewHolder.changePercent.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        } else if (changePercent < 0) {
            viewHolder.currentPrice.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
            viewHolder.changePercent.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            viewHolder.currentPrice.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            viewHolder.changePercent.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
        }

        return view;
    }

    class ViewHolder {
        TextView stockName;
        TextView currentPrice;
        TextView changePercent;
    }
}