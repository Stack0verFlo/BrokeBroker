package Frameworks;

import Entities.Stock;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class StockChart {
    public static void displayStockChart(Stock stock) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Double> priceData = stock.getHistoricalPrices();
        for (int i = 0; i < priceData.size(); i++) {
            dataset.addValue(priceData.get(i), "Preis", String.valueOf(i + 1));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                stock.getName() + " Preisentwicklung",
                "Zeitpunkt",
                "Preis",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame("Aktienkurs Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}