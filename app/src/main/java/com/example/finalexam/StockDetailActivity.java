package com.example.finalexam; // 根据你的项目包名修改

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
        Intent intent = getIntent();
        String stockName = intent.getStringExtra("stockName");
        String stockCode = intent.getStringExtra("stockCode");
        double currentPrice = intent.getDoubleExtra("currentPrice", 0.0);
        double prevClose = intent.getDoubleExtra("prevClose", 0.0);
        double openPrice = intent.getDoubleExtra("openPrice", 0.0);
        double highPrice = intent.getDoubleExtra("highPrice", 0.0);
        double lowPrice = intent.getDoubleExtra("lowPrice", 0.0);
        long volume = intent.getLongExtra("volume", 0);
        double amount = intent.getDoubleExtra("amount", 0.0);

        // 计算涨跌额和涨跌幅
        double changeAmount = currentPrice - prevClose;
        double changePercent = (changeAmount / prevClose) * 100;

        // 设置数据到UI
        stockNameView.setText(stockName);
        stockCodeView.setText(stockCode);

        // 使用DecimalFormat格式化价格和百分比
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        DecimalFormat percentFormat = new DecimalFormat("0.00%");
        DecimalFormat volumeFormat = new DecimalFormat("#,###");

        currentPriceView.setText(priceFormat.format(currentPrice));
        changeAmountView.setText(priceFormat.format(changeAmount));
        changePercentView.setText(percentFormat.format(changePercent / 100));

        // 设置其他行情数据
        openPriceView.setText(priceFormat.format(openPrice));
        highPriceView.setText(priceFormat.format(highPrice));
        lowPriceView.setText(priceFormat.format(lowPrice));
        prevCloseView.setText(priceFormat.format(prevClose));
        volumeView.setText(volumeFormat.format(volume));
        amountView.setText(volumeFormat.format(amount));

        // 根据涨跌情况设置颜色
        if (changeAmount > 0) {
            // 上涨：红色
            currentPriceView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            changeAmountView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            changePercentView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else if (changeAmount < 0) {
            // 下跌：绿色
            currentPriceView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            changeAmountView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            changePercentView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
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
}