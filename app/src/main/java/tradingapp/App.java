package tradingapp;

import tradingapp.connection.IBConnection;
import tradingapp.data.StockData;
import tradingapp.data.StockDataFetcher;
import tradingapp.analysis.TrendAnalyzer;
import tradingapp.scheduler.StockDataScheduler;

public class App {
    public static void main(String[] args) {
        IBConnection ibConnection = new IBConnection();
        StockDataFetcher dataFetcher = new StockDataFetcher(ibConnection.getClient());
        StockData stockData = new StockData(/* parameters */);
        TrendAnalyzer trendAnalyzer = new TrendAnalyzer();

        StockDataScheduler scheduler = new StockDataScheduler(dataFetcher, stockData, trendAnalyzer);
        scheduler.start();
    }
      public EClientSocket getClient() {
        return client;
    }
}
