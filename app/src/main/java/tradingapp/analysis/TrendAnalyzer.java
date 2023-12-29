import java.math.BigDecimal;
import java.util.Queue;

public class TrendAnalyzer {

    public Trend analyzeTrend(Queue<StockPrice> prices, int movingAveragePeriod) {
        // Ensure enough data for SMA calculation
        if (prices == null || prices.isEmpty() || prices.size() < movingAveragePeriod) {
            return null; 
        }

        // StockPrice first = prices.peek();
        // StockPrice last = prices.peek();

        // for (StockPrice price : prices) {
        //     last = price;
        // }

        // if (last == null) {
        //     return null; 
        // }

        // BigDecimal priceChange = last.getPrice().subtract(first.getPrice());
        // String trendType;

        // if (priceChange.compareTo(BigDecimal.ZERO) > 0) {
        //     trendType = "Uptrend";
        // } else if (priceChange.compareTo(BigDecimal.ZERO) < 0) {
        //     trendType = "Downtrend";
        // } else {
        //     trendType = "Sideways";
        // }

        // return new Trend(trendType, first.getPrice(), last.getPrice(), first.getTimestamp(), last.getTimestamp());

        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (StockPrice price : prices) {
            sum = sum.add(price.getPrice());
            if (++count == movingAveragePeriod) break;
        }
        BigDecimal sma = sum.divide(BigDecimal.valueOf(movingAveragePeriod), RoundingMode.HALF_UP);

        StockPrice first = prices.peek();
        StockPrice last = null;
        for (StockPrice price : prices) {
            last = price;
        }

        BigDecimal priceChange = last.getPrice().subtract(first.getPrice());
        String trendType;

        // Compare last price to SMA to determine trend direction
        if (last.getPrice().compareTo(sma) > 0) {
            trendType = "Uptrend";
        } else if (last.getPrice().compareTo(sma) < 0) {
            trendType = "Downtrend";
        } else {
            trendType = "Sideways";
        }

        return new Trend(trendType, first.getPrice(), last.getPrice(), first.getTimestamp(), last.getTimestamp());
    
    }
}
