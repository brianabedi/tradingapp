package tradingapp.connection;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.EWrapperImpl;

public class IBConnection {
    private EClientSocket client;
    private EReaderSignal readerSignal;
    private EWrapper wrapper;

    public IBConnection() {
        wrapper = new EWrapperImpl();
        readerSignal = new EJavaSignal();
        client = new EClientSocket(wrapper, readerSignal);

        // Establish a connection
        client.eConnect("localhost", 7496, 0); // Change the host and port if necessary

        createReaderThread();

    }

    private void createReaderThread() {
            final EReader reader = new EReader(client, readerSignal);
            reader.start();
            new Thread(() -> {
                while (client.isConnected()) {
                    readerSignal.waitForSignal();
                    try {
                        javax.swing.SwingUtilities.invokeAndWait(reader::processMsgs);
                    } catch (Exception e) {
                        e.printStackTrace(); // Consider more sophisticated error handling
                    }
                }
                // Handle disconnection logic here
            }).start();
        }

    public EClientSocket getClient() {
        return client;
    }

 private static class MyEWrapperImpl extends EWrapperImpl {
    @Override
    public void error(Exception e) {
        System.err.println("Error: " + e.getMessage());
        // Implement additional error handling logic here
    }

    @Override
    public void error(String str) {
        System.err.println("Error: " + str);
        // Implement additional error handling logic here
    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {
        System.err.println("Error. ID: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg);
        // Implement additional error handling logic here
    }

    @Override
    public void connectionClosed() {
        System.out.println("Connection closed.");
        // Implement logic to handle disconnection, like trying to reconnect
    }

    // Market data handling
    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        System.out.println("Tick Price. Ticker Id: " + tickerId + ", Field: " + field + ", Price: " + price);
        // Implement market data handling logic here
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
        System.out.println("Tick Size. Ticker Id: " + tickerId + ", Field: " + field + ", Size: " + size);
        // Implement market data handling logic here
    }

    // Other override

}
