package repositories;

public interface PriceUpdateListener {
    void onPriceUpdate(String symbol, double newPrice);
}

