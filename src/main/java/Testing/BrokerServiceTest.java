package Testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Entities.Portfolio;
import Entities.Stock;
import repositories.PortfolioRepository;
import repositories.StockRepository;
import services.BrokerService;
import services.StockService;

class BrokerServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private StockRepository stockRepository;

    private BrokerService brokerService;
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        stockService = mock(StockService.class);
        brokerService = new BrokerService(portfolioRepository, stockService);
    }
    @Test
    void buyStock_whenSymbolNotFoundOrInvalidPrice_ShouldThrowException() {
        String portfolioId = "portfolio123";
        String symbol = "INVALID";
        Portfolio portfolio = new Portfolio("user123", 1000.0);

        when(portfolioRepository.findById(portfolioId)).thenReturn(portfolio);
        when(stockService.getCurrentPrice(symbol)).thenReturn(0.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            brokerService.buyStock(portfolioId, symbol, 1);
        });

        String expectedMessage = "Stock symbol not found or invalid price";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void buyStock_whenSufficientBalance() {
        Portfolio portfolio = new Portfolio("user123", 1000.0);
        when(portfolioRepository.findById("portfolio123")).thenReturn(portfolio);
        when(stockService.getCurrentPrice("AAPL")).thenReturn(150.0);
        // Act
        brokerService.buyStock("portfolio123", "AAPL", 5);

        // Assert
        verify(portfolioRepository).save(portfolio);
        assertEquals(250.0, portfolio.getBalance(), "Balance should be deducted after buying stocks");
    }
    @Test
    void sellStock_whenNotEnoughStock_ShouldThrowException() {
        // Arrange
        String portfolioId = "portfolio123";
        String symbol = "AAPL";
        Portfolio portfolio = new Portfolio("user123", 1000.0);
        when(portfolioRepository.findById(portfolioId)).thenReturn(portfolio);
        Stock appleStock = new Stock("AAPL", 150.0);
        when(stockService.getCurrentPrice(symbol)).thenReturn(appleStock.getCurrentPrice());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            brokerService.sellStock(portfolioId, symbol, 5);
        });
    }

    @Test
    void sellStock_whenStockExists_ShouldDeductStock() {
        // Arrange
        String portfolioId = "portfolio123";
        String symbol = "AAPL";
        int quantity = 5;
        double sellPrice = 200.0;
        Portfolio portfolio = new Portfolio("user123", 1000.0);
        portfolio.addStock(symbol, 10, 150.0); // Add some stock first
        when(portfolioRepository.findById(portfolioId)).thenReturn(portfolio);
        when(stockService.getCurrentPrice(symbol)).thenReturn(sellPrice);

        // Act
        brokerService.sellStock(portfolioId, symbol, quantity);

        // Assert
        verify(portfolioRepository).save(portfolio);
        assertEquals(5, portfolio.getStocks().get(0).getQuantity());
        assertEquals(1000.0 + quantity * sellPrice, portfolio.getBalance());
    }

    @Test
    void sellStock_whenPortfolioDoesNotExist_ShouldThrowException() {
        // Arrange
        String portfolioId = "nonExistingId";
        when(portfolioRepository.findById(portfolioId)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            brokerService.sellStock(portfolioId, "AAPL", 1);
        });
    }

    @Test
    void buyStock_whenNotEnoughBalance_ShouldThrowException() {
        // Arrange
        String portfolioId = "portfolio123";
        Portfolio portfolio = new Portfolio("user123", 100.0); // Not enough balance
        when(portfolioRepository.findById(portfolioId)).thenReturn(portfolio);
        when(stockService.getCurrentPrice("AAPL")).thenReturn(150.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            brokerService.buyStock(portfolioId, "AAPL", 1);
        });
    }
}
