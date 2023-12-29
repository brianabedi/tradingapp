package tradingapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.Queue;
import tradingapp.data.StockData;
import tradingapp.data.StockDataFetcher;
import tradingapp.analysis.TrendAnalyzer;
import tradingapp.analysis.HistoricalDataFetcher;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainApp extends Application {
    private LineChart<Number, Number> lineChart;
    private StockData stockData;


    @Override
    public void start(Stage stage) {
        stage.setTitle("Stock Trend Visualization");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Price");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Stock Trends");

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
        
        this.stockData = new StockData(50); 

        boolean useHistoricalData = true;

        if (useHistoricalData) {
            performHistoricalDataAnalysis();
        } else {
            startRealTimeDataFetching();
        }
    }

    private void performHistoricalDataAnalysis() {
        HistoricalDataFetcher historicalFetcher = new HistoricalDataFetcher(ibConnection.getClient(), stockData);
        historicalFetcher.fetchHistoricalData("AAPL", "2 D", "1 hour");
        processHistoricalData(stockData);
        updateHistoricalChart(stockData);
    }
    private void processHistoricalData(StockData stockData) {
        TrendAnalyzer trendAnalyzer = new TrendAnalyzer();
        Trend identifiedTrend = trendAnalyzer.analyzeTrend(stockData.getPrices(), 10);

        if (identifiedTrend != null) {
            System.out.println("Identified Trend: " + identifiedTrend);
        }
    }

    private void updateHistoricalChart(StockData stockData) {
        Platform.runLater(() -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Historical Data");

            int xValue = 0;
            for (StockPrice price : stockData.getPrices()) {
                series.getData().add(new XYChart.Data<>(xValue++, price.getPrice().doubleValue()));
            }

            lineChart.getData().clear();
            lineChart.getData().add(series);
        });
    }   


    private void startDataFetchAndAnalysis() {
        IBConnection ibConnection = new IBConnection();
        // StockDataFetcher dataFetcher = new StockDataFetcher(ibConnection.getClient());
        StockDataFetcher dataFetcher = new StockDataFetcher(ibConnection.getClient(), this.stockData, this);

        StockData stockData = new StockData(50); // Assuming a maximum of 50 data points
        TrendAnalyzer trendAnalyzer = new TrendAnalyzer();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            // try {
            //     StockPrice currentPrice = dataFetcher.getStockPrice("AAPL");
            //     stockData.addPrice(currentPrice);
            //     updateLineChart(stockData.getPrices());
            //     Trend trend = trendAnalyzer.analyzeTrend(stockData.getPrices());
            //     // Log or display the trend
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
            dataFetcher.getStockPrice("AAPL");

        }, 0, 1, TimeUnit.SECONDS);
    }

    private void updateLineChart(Queue<StockPrice> prices) {
        // XYChart.Series<Number, Number> series = new XYChart.Series<>();
        // int xValue = 0;
        // for (StockPrice price : prices) {
        //     series.getData().add(new XYChart.Data<>(xValue++, price.getPrice().doubleValue()));
        // }
        // lineChart.getData().clear();
        // lineChart.getData().add(series);
        Platform.runLater(() -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            int xValue = 0;
            for (StockPrice price : prices) {
                series.getData().add(new XYChart.Data<>(xValue++, price.getPrice().doubleValue()));
            }
            lineChart.getData().clear();
            lineChart.getData().add(series);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
