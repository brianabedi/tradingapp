import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.Queue;

public class StockData {
    private Queue<StockPrice> prices;
    private int maxSize;

    public StockData(int maxSize) {
        // this.prices = new LinkedList<>();
        this.prices = new ConcurrentLinkedQueue<>(); 
        this.maxSize = maxSize; 
    }

    public void addPrice(StockPrice price) {
        if (prices.size() >= maxSize) {
            prices.poll();
        }
        prices.add(price);
    }
    
    public synchronized StockPrice getLatestPrice() {
        return prices.peek();
    }

    public synchronized Queue<StockPrice> getPrices() {
        return new LinkedList<>(prices);
    }
}

public class StockPrice {
    private LocalDateTime timestamp;
    private BigDecimal price;

    public StockPrice(LocalDateTime timestamp, BigDecimal price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
