package services;

import Entities.Portfolio;
import Entities.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.PortfolioRepository;
import repositories.StockRepository;
import services.PortfolioService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;
    @Mock
    private StockRepository stockRepository;

    private PortfolioService portfolioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        portfolioService = new PortfolioService(portfolioRepository, stockRepository);
    }

    @Test
    void shouldAddStockToPortfolioWhenStockExists() {
        // Arrange
        String portfolioId = "portfolioId";
        String symbol = "AAPL";
        int quantity = 10;
        double price = 100.0;
        Portfolio portfolio = mock(Portfolio.class);
        Stock stock = new Stock(symbol, price);

        when(portfolioRepository.findById(portfolioId)).thenReturn(portfolio);
        when(stockRepository.findStockBySymbol(symbol)).thenReturn(stock);

        // Act
        portfolioService.addStockToPortfolio(portfolioId, symbol, quantity);

        // Assert
        verify(portfolio).addStock(symbol, quantity, price);
        verify(portfolioRepository).save(portfolio);
    }

    @Test
    void shouldNotAddStockToPortfolioWhenStockDoesNotExist() {
        // Arrange
        String portfolioId = "portfolioId";
        String symbol = "AAPL";
        int quantity = 10;

        when(portfolioRepository.findById(portfolioId)).thenReturn(new Portfolio("userId", 5000.0));
        when(stockRepository.findStockBySymbol(symbol)).thenReturn(null);

        // Act
        portfolioService.addStockToPortfolio(portfolioId, symbol, quantity);

        // Assert
        verify(portfolioRepository, never()).save(any(Portfolio.class));
    }
    @Test
    void shouldRemoveStockFromPortfolioWhenStockExists() {
        // Arrange
        String portfolioId = "portfolioId";
        String symbol = "AAPL";
        int quantity = 5;
        Portfolio portfolio = new Portfolio("userId", 5000.0);
        portfolio.addStock(symbol, 10, 100.0); // Assuming the portfolio already has 10 AAPL stocks

        when(portfolioRepository.findById(portfolioId)).thenReturn(portfolio);

        // Act
        portfolioService.removeStockFromPortfolio(portfolioId, symbol, quantity);

        // Assert
        assertEquals(5, portfolio.getStocks().get(0).getQuantity());
        verify(portfolioRepository).save(portfolio);
    }

    @Test
    void shouldGetPortfolioByUserId() {
        // Arrange
        String userId = "userId";
        Portfolio expectedPortfolio = new Portfolio(userId, 5000.0);
        when(portfolioRepository.findByUserId(userId)).thenReturn(expectedPortfolio);

        // Act
        Portfolio actualPortfolio = portfolioService.getPortfolioByUserId(userId);

        // Assert
        assertEquals(expectedPortfolio, actualPortfolio);
    }
}
