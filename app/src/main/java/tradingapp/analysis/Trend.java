public class Trend {
    private String type;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Trend(String type, BigDecimal startPrice, BigDecimal endPrice, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        if (startPrice.compareTo(BigDecimal.ZERO) < 0 || endPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Prices must not be negative.");
        }
       
        this.type = type;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public String getType() {
        return type;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Trend{" +
            "type='" + type + '\'' +
            ", startPrice=" + startPrice +
            ", endPrice=" + endPrice +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
    }
}
