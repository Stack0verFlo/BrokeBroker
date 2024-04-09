package Frameworks;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.util.List;

public class StockChart {
    public static void displayStockChart(String symbol, List<Double> priceData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Hinzufügen jedes Preiswertes mit einem Index als Kategorie
        for (int i = 0; i < priceData.size(); i++) {
            dataset.addValue(priceData.get(i), "Preis", String.valueOf(i + 1)); // Nutzen von i+1 für die Darstellung, beginnend mit 1
        }

        JFreeChart chart = ChartFactory.createLineChart(
                symbol + " Preisentwicklung",
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

