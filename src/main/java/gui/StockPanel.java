package gui;

import controllers.StockController;
import Entities.Stock;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class StockPanel extends JPanel {
    private final StockController stockController;
    private JComboBox<String> stockSymbolComboBox;
    private JButton updatePriceButton;
    private ChartPanel chartPanel;

    public StockPanel() {
        this.stockController = new StockController();
        setLayout(new BorderLayout());
        initializeComponents();
        add(createControlPanel(), BorderLayout.NORTH);
        updateChart(stockController.getAllSymbols().get(0)); // Initialisiere das Chart mit dem ersten Symbol
    }

    private void initializeComponents() {
        List<String> allSymbols = stockController.getAllSymbols();
        stockSymbolComboBox = new JComboBox<>(allSymbols.toArray(new String[0]));
        stockSymbolComboBox.addActionListener(this::handleUpdatePriceAction);
        updatePriceButton = new JButton("Update Price");
        updatePriceButton.addActionListener(this::handleUpdatePriceAction);
        chartPanel = new ChartPanel(null);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Wähle Aktie:"));
        panel.add(stockSymbolComboBox);
        panel.add(updatePriceButton);
        return panel;
    }

    private void handleUpdatePriceAction(ActionEvent e) {
        String symbol = (String) stockSymbolComboBox.getSelectedItem();
        stockController.updateStockPrice(symbol);
        updateChart(symbol);
    }

    private void updateChart(String symbol) {
        Stock stock = stockController.getStock(symbol);
        XYSeries series = new XYSeries("Historische Preise");
        List<Double> prices = stock.getHistoricalPrices();
        for (int i = 0; i < prices.size(); i++) {
            series.add(i + 1, prices.get(i));
        }
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
            remove(chartPanel);
        }
        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }
}
