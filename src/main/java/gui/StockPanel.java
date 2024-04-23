package gui;

import controllers.StockController;
import Entities.Stock;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import services.StockService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.List;

public class StockPanel extends JPanel {
    private final StockController stockController;
    private JComboBox<String> stockSymbolComboBox;
    private JButton updatePriceButton;
    private JLabel currentPriceLabel;
    private ChartPanel chartPanel;
    private DecimalFormat priceFormat;
    private final StockService stockService;
    private PortfolioPanel portfolioPanel;

    public StockPanel(StockService stockService) {
        this.stockController = new StockController(stockService);
        this.stockService = stockService;
        this.priceFormat = new DecimalFormat("#.00");
        setLayout(new BorderLayout());
        initializeComponents();
        add(createControlPanel(), BorderLayout.NORTH);
        chartPanel = new ChartPanel(null); // Initialisiere das ChartPanel explizit mit null
        add(chartPanel, BorderLayout.CENTER); // Füge das ChartPanel direkt nach der Initialisierung hinzu
        initializeChart();  // Setup the initial chart using symbols
        this.stockService.setPriceUpdateListener(portfolioPanel);
    }

    private void initializeComponents() {
        List<String> allSymbols = stockController.getAllSymbols();
        stockSymbolComboBox = new JComboBox<>(allSymbols.toArray(new String[0]));
        stockSymbolComboBox.addActionListener(this::handleStockSelectionAction);
        updatePriceButton = new JButton("Update Price");
        updatePriceButton.addActionListener(this::handleUpdatePriceAction);
        currentPriceLabel = new JLabel();
        updateCurrentPriceDisplay(allSymbols.get(0)); // Displays the current price for the first symbol
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Wähle Aktie:"));
        panel.add(stockSymbolComboBox);
        panel.add(new JLabel("Aktueller Preis:"));
        panel.add(currentPriceLabel);
        panel.add(updatePriceButton);
        return panel;
    }

    private void initializeChart() {
        String initialSymbol = stockController.getAllSymbols().get(0);
        if (initialSymbol != null) {
            updateChart(initialSymbol); // Initialize the chart with the first symbol
        }
    }

    private void handleStockSelectionAction(ActionEvent e) {
        String symbol = (String) stockSymbolComboBox.getSelectedItem();
        updateCurrentPriceDisplay(symbol);
        updateChart(symbol);
    }

    private void handleUpdatePriceAction(ActionEvent e) {
        String symbol = (String) stockSymbolComboBox.getSelectedItem();
        stockService.updateStockPrice(symbol); // Notifies listeners, including PortfolioPanel
        updateChart(symbol);
        updateCurrentPriceDisplay(symbol); // Updates the price on the StockPanel
    }

    private void updateCurrentPriceDisplay(String symbol) {
        double price = stockService.getCurrentPrice(symbol);
        currentPriceLabel.setText(String.format("Current Price: %.2f", price));
    }

    private void updateChart(String symbol) {
        SwingUtilities.invokeLater(() -> {
            JFreeChart chart = createChart(symbol);
            setupChartPanel(chart);
        });
    }

    private JFreeChart createChart(String symbol) {
        XYSeriesCollection dataset = createDataset(symbol);
        return ChartFactory.createXYLineChart(
                symbol + " Preisverlauf",
                "Zeitpunkt",
                "Preis",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
    }

    private XYSeriesCollection createDataset(String symbol) {
        Stock stock = stockController.getStock(symbol);
        XYSeries series = new XYSeries("Historische Preise");
        List<Double> prices = stock.getHistoricalPrices(40);
        for (int i = 0; i < prices.size(); i++) {
            series.add(i, prices.get(i));
        }
        series.add(prices.size(), stock.getCurrentPrice());
        return new XYSeriesCollection(series);
    }

    private void setupChartPanel(JFreeChart chart) {
        if (chartPanel != null) {
            this.remove(chartPanel);
        }
        chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
