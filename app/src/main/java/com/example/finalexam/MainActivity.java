package com.example.finalexam;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<HashMap<String, String>> listItems;
    private StockAdapter listItemAdapter;
    private Handler handler;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        initListView();
        setListAdapter(listItemAdapter);

        // 设置列表项点击监听
        getListView().setOnItemClickListener(this);

        // 初始化Handler用于接收网络线程的数据
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    listItems = (ArrayList<HashMap<String, String>>) msg.obj;
                    listItemAdapter.clear();
                    listItemAdapter.addAll(listItems);
                    listItemAdapter.notifyDataSetChanged();
                }
            }
        };

        // 启动线程获取股票数据
        loadStockData();
    }

    private void initListView() {
        listItems = new ArrayList<>();
        listItemAdapter = new StockAdapter(
                this,
                R.layout.list_item,  // 对应list_item.xml布局
                listItems
        );
    }

    private void loadStockData() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

        new Thread(() -> {
            try {
                // 新浪财经实时数据接口（获取沪深A股前20只股票）
                String url = "https://hq.sinajs.cn/list=sh000001,sh600519,sh601318,sz000858,sh601888,sh601398,sh601988,sh601288,sh601988,sh601818";
                Document doc = Jsoup.connect(url)
                        .header("Referer", "https://finance.sina.com.cn")
                        .ignoreContentType(true)
                        .timeout(10000)
                        .get();
                
                // 解析返回的数据
                String[] stockDataArray = doc.body().text().split(";");
                List<HashMap<String, String>> stocks = new ArrayList<>();
                
                for (String stockData : stockDataArray) {
                    if (stockData.trim().isEmpty()) continue;
                    
                    // 示例数据格式: var hq_str_sh600519="贵州茅台,1920.50,...";
                    String[] parts = stockData.split("=");
                    if (parts.length < 2) continue;
                    
                    String[] values = parts[1].replace("\"", "").split(",");
                    if (values.length < 2) continue;

                    HashMap<String, String> stock = new HashMap<>();
                    stock.put("stockName", values[0]);  // 股票名称
                    stock.put("stockCode", parts[0].substring(parts[0].lastIndexOf("_") + 1)); // 股票代码
                    stock.put("currentPrice", values[1]); // 最新价
                    
                    // 计算涨跌幅（需要昨收价）
                    if (values.length > 2 && !values[2].isEmpty()) {
                        double prevClose = Double.parseDouble(values[2]);
                        double currentPrice = Double.parseDouble(values[1]);
                        double changePercent = ((currentPrice - prevClose) / prevClose) * 100;
                        stock.put("changePercent", String.format("%.2f%%", changePercent));
                    }
                    
                    stocks.add(stock);
                }

                // 发送数据到主线程更新UI
                Message msg = handler.obtainMessage(1, stocks);
                handler.sendMessage(msg);

            } catch (Exception e) {
                Log.e(TAG, "获取股票数据失败: " + e.getMessage());
            } finally {
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> stock = listItems.get(position);

        Intent intent = new Intent(this, StockDetailActivity.class);
        intent.putExtra("stockName", stock.get("stockName"));
        intent.putExtra("stockCode", "000001"); // 添加默认股票代码
        intent.putExtra("currentPrice", Double.parseDouble(stock.get("currentPrice")));
        intent.putExtra("prevClose", Double.parseDouble(stock.get("currentPrice")) - 1); // 模拟昨收价
        // 其他字段使用默认值
        intent.putExtra("openPrice", Double.parseDouble(stock.get("currentPrice")) - 0.5);
        intent.putExtra("highPrice", Double.parseDouble(stock.get("currentPrice")) + 0.5);
        intent.putExtra("lowPrice", Double.parseDouble(stock.get("currentPrice")) - 0.5);
        intent.putExtra("volume", 1000000L);
        intent.putExtra("amount", 5000000.0);

        startActivity(intent);
    }
}