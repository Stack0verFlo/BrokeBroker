import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.util.Map;

public class StockChart {
    public static void displayStockChart(String symbol, Map<String, Double> priceData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        priceData.forEach((date, price) -> dataset.addValue(price, "Preis", date));

        JFreeChart chart = ChartFactory.createLineChart(
                symbol + " Preisentwicklung",
                "Zeit",
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
