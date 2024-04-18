package gui;

import controllers.StockController;
import Entities.Stock;
import services.StockService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class StockPanel extends JPanel {
    private final StockController stockController;
    private JButton updatePriceButton;
    private JComboBox<String> symbolComboBox;

    public StockPanel() {
        this.stockController = new StockController();
        setLayout(new BorderLayout());
        add(createPriceUpdatePanel(), BorderLayout.NORTH);
        add(createChartPanel("AAPL"), BorderLayout.CENTER);  // Beispiel-Aktiensymbol "AAPL"
    }

    private JPanel createPriceUpdatePanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel symbolLabel = new JLabel("Symbol:");
        panel.add(symbolLabel);

        symbolComboBox = new JComboBox<>(stockController.getAllSymbols());
        panel.add(symbolComboBox);

        updatePriceButton = new JButton("Update Price");
        updatePriceButton.addActionListener(this::handleUpdatePriceAction);
        panel.add(updatePriceButton);

        return panel;
    }

    private void handleUpdatePriceAction(ActionEvent e) {
        String symbol = (String) symbolComboBox.getSelectedItem();
        //stockController.updateStockPrice(symbol, price);
        refreshChart(symbol);  // Refresh the chart with new data
    }

    private ChartPanel createChartPanel(String symbol) {
        XYSeries series = new XYSeries("Historical Prices");
        Stock stock = stockController.getStock(symbol);
        if (stock != null) {
            int index = 1;
            for (Double price : stock.getHistoricalPrices()) {
                series.add(index++, price);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Stock Price History",
                "Time",
                "Price",
                dataset,
                PlotOrientation.VERTICAL,
                true,   // legend
                true,   // tooltips
                false   // URLs
        );

        return new ChartPanel(chart);
    }

    private void refreshChart(String symbol) {
        remove(1); // Remove the existing chart
        add(createChartPanel(symbol), BorderLayout.CENTER);
        validate();
        repaint();
    }
}