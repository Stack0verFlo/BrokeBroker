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
import gui.PortfolioPanel;

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
    private StockService stockService;
    private PortfolioPanel portfolioPanel;

    public StockPanel(StockService stockService) {
        this.stockController = new StockController(stockService);
        this.stockService = stockService;
        this.priceFormat = new DecimalFormat("#.00");
        setLayout(new BorderLayout());
        initializeComponents();
        add(createControlPanel(), BorderLayout.NORTH);
        updateChart(stockController.getAllSymbols().get(0)); // Initialisiere das Chart mit dem ersten Symbol
        add(chartPanel, BorderLayout.CENTER);
        this.stockService.setPriceUpdateListener(portfolioPanel);
    }

    private void initializeComponents() {
        List<String> allSymbols = stockController.getAllSymbols();
        stockSymbolComboBox = new JComboBox<>(allSymbols.toArray(new String[0]));
        stockSymbolComboBox.addActionListener(this::handleStockSelectionAction);
        updatePriceButton = new JButton("Update Price");
        updatePriceButton.addActionListener(this::handleUpdatePriceAction);
        currentPriceLabel = new JLabel();
        chartPanel = new ChartPanel(null);
        updateCurrentPriceDisplay(allSymbols.get(0)); // Zeigt den aktuellen Preis für das erste Symbol an
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

    private void handleStockSelectionAction(ActionEvent e) {
        String symbol = (String) stockSymbolComboBox.getSelectedItem();
        updateCurrentPriceDisplay(symbol);
        updateChart(symbol);
    }

    private void handleUpdatePriceAction(ActionEvent e) {
        String symbol = (String) stockSymbolComboBox.getSelectedItem();
        stockService.updateStockPrice(symbol); // Benachrichtigt Listener, einschließlich PortfolioPanel
        updateChart(symbol);
        updateCurrentPriceDisplay(symbol); // Aktualisiert den Preis auf dem StockPanel
    }

    private void updateCurrentPriceDisplay(String symbol) {
        double price = stockService.getCurrentPrice(symbol);
        currentPriceLabel.setText(String.format("Current Price: %.2f", price));
    }

    private void updateChart(String symbol) {
        SwingUtilities.invokeLater(() -> {
            Stock stock = stockController.getStock(symbol);
            XYSeries series = new XYSeries("Historische Preise");

            // Hier verwenden Sie die neue Methode, um nur die letzten 40 Preise zu bekommen
            List<Double> prices = stock.getHistoricalPrices(40);
            for (int i = 0; i < prices.size(); i++) {
                series.add(i, prices.get(i));
            }
            // Fügen Sie den aktuellen Preis als letzten Datenpunkt hinzu
            series.add(prices.size(), stock.getCurrentPrice());

            XYSeriesCollection dataset = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYLineChart(
                    symbol + " Preisverlauf",
                    "Zeitpunkt",
                    "Preis",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false);

            if (chartPanel != null) {
                this.remove(chartPanel);
            }
            chartPanel = new ChartPanel(chart);
            this.add(chartPanel, BorderLayout.CENTER);

            this.revalidate();
            this.repaint();
        });
    }
}
