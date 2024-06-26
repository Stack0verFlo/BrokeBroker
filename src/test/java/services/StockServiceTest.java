package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Entities.Stock;
import repositories.StockRepository;
import repositories.PriceUpdateListener;
import services.StockService;

import java.util.Arrays;
import java.util.List;

class StockServiceTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private PriceUpdateListener priceUpdateListener;

    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        stockService = new StockService(stockRepository);
    }

    @Test
    void shouldInitializeStocksWhenEmpty() {
        when(stockRepository.isStockListEmpty()).thenReturn(true);
        stockService.initializeStocks();
        verify(stockRepository, times(10)).saveStock(any(Stock.class));
    }

    @Test
    void shouldNotInitializeStocksWhenNotEmpty() {
        when(stockRepository.isStockListEmpty()).thenReturn(false);
        stockService.initializeStocks();
        verify(stockRepository, never()).saveStock(any(Stock.class));
    }

    @Test
    void updateStockPriceShouldUpdateAndNotifyListener() {
        Stock stock = new Stock("AAPL", 100);
        when(stockRepository.findStockBySymbol("AAPL")).thenReturn(stock);
        stockService.setPriceUpdateListener(priceUpdateListener);

        stockService.updateStockPrice("AAPL");

        verify(stockRepository).saveStock(stock);
        verify(priceUpdateListener).onPriceUpdate(eq("AAPL"), anyDouble());
        assertNotEquals(100.0, stock.getCurrentPrice());
    }

    @Test
    void updateStockPriceShouldNotGoBelowOne() {
        Stock stock = new Stock("AAPL", 1.5);
        when(stockRepository.findStockBySymbol("AAPL")).thenReturn(stock);
        stockService.updateStockPrice("AAPL");
        verify(stockRepository).saveStock(stock);
        assertTrue(stock.getCurrentPrice() >= 1.0);
    }

    @Test
    void getStockShouldReturnCorrectStock() {
        Stock apple = new Stock("AAPL", 150);
        when(stockRepository.findStockBySymbol("AAPL")).thenReturn(apple);

        Stock result = stockService.getStock("AAPL");
        assertEquals(apple, result);
    }

    @Test
    void getAllSymbolsShouldReturnListOfSymbols() {
        List<Stock> stocks = Arrays.asList(new Stock("AAPL", 150), new Stock("GOOGL", 1000));
        when(stockRepository.findAllStocks()).thenReturn(stocks);

        List<String> symbols = stockService.getAllSymbols();
        assertEquals(Arrays.asList("AAPL", "GOOGL"), symbols);
    }

    @Test
    void updateStockPriceShouldDoNothingWhenStockNotFound() {
        when(stockRepository.findStockBySymbol("UNKNOWN")).thenReturn(null);
        stockService.updateStockPrice("UNKNOWN");
        verify(stockRepository, never()).saveStock(any(Stock.class));
        verifyNoInteractions(priceUpdateListener);
    }
}
