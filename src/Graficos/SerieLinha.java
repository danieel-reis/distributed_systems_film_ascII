/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graficos;

/**
 *
 * @author daniel
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class SerieLinha extends ApplicationFrame {

    public SerieLinha(String name_img, String title, String nome_x, String nome_y, ArrayList<Integer> dados_x, ArrayList<Long> dados_y, int dimension_x, int dimension_y) throws FileNotFoundException, IOException {

        super("Gráfico");
        final XYSeries series = new XYSeries("Tempo de execução");

        for (int i = 0; i < dados_x.size() && i < dados_y.size(); i++) {
            series.add(dados_x.get(i), dados_y.get(i));
        }
        
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart grafico = ChartFactory.createXYLineChart(title, nome_x, nome_y,
                data, PlotOrientation.VERTICAL, true, true, false);
        final ChartPanel chartPanel = new ChartPanel(grafico);
        chartPanel.setPreferredSize(new java.awt.Dimension(dimension_x, dimension_y));
        setContentPane(chartPanel);
        
        OutputStream arquivo = new FileOutputStream(name_img);
        ChartUtilities.writeChartAsPNG(arquivo, grafico, dimension_x, dimension_y);
    }
}
