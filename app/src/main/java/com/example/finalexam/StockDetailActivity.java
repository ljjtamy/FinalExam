package com.example.finalexam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class StockDetailActivity extends AppCompatActivity {

    private TextView stockNameView, stockCodeView, currentPriceView;
    private TextView changeAmountView, changePercentView;
    private TextView openPriceView, highPriceView, lowPriceView;
    private TextView prevCloseView, volumeView, amountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        // 初始化UI组件
        initViews();

        // 获取从MainActivity传递过来的数据
        initAndSetData();
    }

    private void initViews() {
        stockNameView = findViewById(R.id.stockName);
        stockCodeView = findViewById(R.id.stockCode);
        currentPriceView = findViewById(R.id.currentPrice);
        changeAmountView = findViewById(R.id.changeAmount);
        changePercentView = findViewById(R.id.changePercent);
        openPriceView = findViewById(R.id.openPrice);
        highPriceView = findViewById(R.id.highPrice);
        lowPriceView = findViewById(R.id.lowPrice);
        prevCloseView = findViewById(R.id.prevClose);
        volumeView = findViewById(R.id.volume);
        amountView = findViewById(R.id.amount);
    }

    private void initAndSetData() {
        Intent intent = getIntent();

        // 使用Bundle简化数据获取
        Bundle extras = intent.getExtras();
        String stockName = extras.getString("stockName");
        String stockCode = extras.getString("stockCode");
        double currentPrice = extras.getDouble("currentPrice");
        double prevClose = extras.getDouble("prevClose");
        double openPrice = extras.getDouble("openPrice");
        double highPrice = extras.getDouble("highPrice");
        double lowPrice = extras.getDouble("lowPrice");
        long volume = extras.getLong("volume");
        double amount = extras.getDouble("amount");

        // 计算涨跌
        double changeAmount = currentPrice - prevClose;
        double changePercent = (changeAmount / prevClose) * 100;

        // 设置基本数据
        stockNameView.setText(stockName);
        stockCodeView.setText(stockCode);

        // 格式化数据
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        currentPriceView.setText(priceFormat.format(currentPrice));
        changeAmountView.setText(priceFormat.format(changeAmount));
        changePercentView.setText(new DecimalFormat("0.00%").format(changePercent / 100));

        // 设置详细数据
        openPriceView.setText(priceFormat.format(openPrice));
        highPriceView.setText(priceFormat.format(highPrice));
        lowPriceView.setText(priceFormat.format(lowPrice));
        prevCloseView.setText(priceFormat.format(prevClose));
        volumeView.setText(String.format("%,d", volume));
        amountView.setText(String.format("%,.2f", amount));

        // 设置颜色
        int color = changeAmount > 0 ? android.R.color.holo_red_dark :
                changeAmount < 0 ? android.R.color.holo_green_dark :
                        android.R.color.primary_text_dark;
        currentPriceView.setTextColor(getResources().getColor(color));
        changeAmountView.setTextColor(getResources().getColor(color));
        changePercentView.setTextColor(getResources().getColor(color));
    }
}