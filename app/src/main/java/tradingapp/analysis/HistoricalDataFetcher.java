public class HistoricalDataFetcher extends EWrapperImpl {
    private EClientSocket client;
    private int requestId;
    private StockData stockData;

    public HistoricalDataFetcher(EClientSocket client, StockData stockData) {
        this.client = client;
        this.requestId = 2000;
        this.stockData = stockData;
    }

    public void fetchHistoricalData(String stockSymbol, String duration, String barSize) {
        Contract contract = new Contract();

        contract.symbol(stockSymbol);
        contract.secType("STK"); 
        contract.currency("USD");
        contract.exchange("SMART");

        String endDateTime = ""; 
        String whatToShow = "TRADES";
        int useRTH = 1;
        int formatDate = 1;

        client.reqHistoricalData(requestId++, contract, endDateTime, duration, barSize, whatToShow, useRTH, formatDate, false, null);
    }

    public void historicalDataReceived(Bar bar) {
        
        LocalDateTime timestamp = LocalDateTime.parse(bar.time(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        BigDecimal price = BigDecimal.valueOf(bar.close());

        stockData.addPrice(new StockPrice(timestamp, price));
    }

    @Override
    public void historicalData(int reqId, Bar bar) {

        StockPrice price = new StockPrice(LocalDateTime.parse(bar.time()), BigDecimal.valueOf(bar.close()));
        stockData.addPrice(price);
    }

    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        System.out.println("Completed receiving historical data");
    }
}
