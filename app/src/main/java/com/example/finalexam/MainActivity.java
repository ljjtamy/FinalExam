package com.example.finalexam; // 根据你的项目包名修改

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<HashMap<String, String>> listItems;
    private StockAdapter listItemAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                R.layout.list_item,  // 对应你创建的list_item.xml布局
                listItems
        );
    }

    private void loadStockData() {
        new Thread(() -> {
            try {
                // 使用腾讯财经API获取股票数据
                Document doc = Jsoup.connect("https://qt.gtimg.cn/q=").get();

                // 解析HTML获取股票数据（这里需要根据实际网页结构调整选择器）
                Elements stockElements = doc.select("div.stock-item");
                List<HashMap<String, String>> stocks = new ArrayList<>();

                for (int i = 0; i < 10; i++) {  // 示例：获取10条股票数据
                    HashMap<String, String> stock = new HashMap<>();
                    stock.put("stockName", "股票" + i);
                    stock.put("currentPrice", String.format("%.2f", Math.random() * 100));

                    double changePercent = (Math.random() - 0.5) * 10;
                    stock.put("changePercent", String.format("%.2f%%", changePercent));

                    stocks.add(stock);
                }

                // 发送数据到主线程更新UI
                Message msg = handler.obtainMessage(1, stocks);
                handler.sendMessage(msg);

            } catch (IOException e) {
                Log.e(TAG, "获取股票数据失败: " + e.getMessage());
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