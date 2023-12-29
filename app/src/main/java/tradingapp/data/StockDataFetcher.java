
import com.ib.client.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockDataFetcher extends EWrapperImpl{
    private EClientSocket client;
    private int requestId;
    private StockData stockData;
    private MainApp mainAppInstance; 

    public StockDataFetcher(EClientSocket client, StockData stockData, MainApp mainAppInstance) {
        this.client = client;
        this.requestId = 1000;
        this.stockData = stockData;
        this.mainAppInstance = mainAppInstance;

    }

    public void getStockPrice(String stockSymbol) {
        Contract contract = new Contract();
        contract.symbol(stockSymbol);
        contract.secType("STK");
        contract.currency("USD");
        contract.exchange("SMART");

        client.reqMktData(requestId++, contract, "", false, false, null);
    }

    public void marketDataReceived(int requestId, double price, ...) {
        System.out.println("Price for request " + requestId + ": " + price);
        stockData.addPrice(new StockPrice(LocalDateTime.now(), BigDecimal.valueOf(price)));
        mainAppInstance.updateLineChart(); 
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attrib) {
        if (field == TickType.LAST.index()) { 
            marketDataReceived(tickerId, price);
            // mainAppInstance.updateLineChart();
        }
    }

}