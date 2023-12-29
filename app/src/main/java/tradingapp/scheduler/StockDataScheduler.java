package tradingapp.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import tradingapp.data.StockData;
import tradingapp.data.StockDataFetcher;
import tradingapp.analysis.TrendAnalyzer;

public class StockDataScheduler {
    private final StockDataFetcher dataFetcher;
    private final StockData stockData;
    private final TrendAnalyzer trendAnalyzer;

    public StockDataScheduler(StockDataFetcher dataFetcher, StockData stockData, TrendAnalyzer trendAnalyzer) {
        this.dataFetcher = dataFetcher;
        this.stockData = stockData;
        this.trendAnalyzer = trendAnalyzer;
    }

    public void start() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            try {
                StockPrice currentPrice = dataFetcher.getStockPrice("AAPL");
                stockData.addPrice(currentPrice);
                Trend trend = trendAnalyzer.analyzeTrend(stockData.getPrices());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
