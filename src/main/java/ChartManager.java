import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import javax.swing.JPanel;

public class ChartManager {

    public JPanel createStockPriceChart(String stockSymbol, TimeSeries priceSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection(priceSeries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                stockSymbol + " Preisentwicklung", // Titel
                "Datum", // X-Achsen-Beschriftung
                "Preis", // Y-Achsen-Beschriftung
                dataset,
                false, // Legende
                true, // Tooltips
                false); // URLs
        return new ChartPanel(chart);
    }
}
