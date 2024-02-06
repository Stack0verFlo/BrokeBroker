import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class StockChart {

    public static void displayStockChart(String symbol) {
        try {
            Stock stock = YahooFinance.get(symbol, true);
            TimeSeries series = new TimeSeries("Stock Price");

            stock.getHistory().forEach(historicalQuote -> {
                series.add(new Day(historicalQuote.getDate().getTime()), historicalQuote.getClose().doubleValue());
            });

            TimeSeriesCollection dataset = new TimeSeriesCollection(series);
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    symbol + " Price Chart",
                    "Date",
                    "Price",
                    dataset,
                    false,
                    true,
                    false);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.add(chartPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setTitle("Stock History Chart");
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
