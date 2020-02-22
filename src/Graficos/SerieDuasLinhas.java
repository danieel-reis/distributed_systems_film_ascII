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
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class SerieDuasLinhas extends ApplicationFrame {

    public SerieDuasLinhas(String nomelinha1, String nomelinha2, String name_img, String title, String nome_x, String nome_y,
            ArrayList<String> dados_x, ArrayList<Long> dados_y, ArrayList<Long> dados_z, int dimension_x, int dimension_y) throws FileNotFoundException, IOException {

        super("Gráfico");
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for(int i = 0; i < dados_x.size() && i < dados_y.size(); i++) {
            ds.addValue(dados_y.get(i), nomelinha1, dados_x.get(i));
        }
        for(int i = 0; i < dados_x.size() && i < dados_z.size(); i++) {
            ds.addValue(dados_z.get(i), nomelinha2, dados_x.get(i));
        }

        JFreeChart grafico = ChartFactory.createLineChart(title, nome_x,
                nome_y, ds, PlotOrientation.VERTICAL, true, true, false);

        final ChartPanel chartPanel = new ChartPanel(grafico);
        chartPanel.setPreferredSize(new java.awt.Dimension(dimension_x, dimension_y));
        setContentPane(chartPanel);

        OutputStream arquivo = new FileOutputStream(name_img);
        ChartUtilities.writeChartAsPNG(arquivo, grafico, dimension_x, dimension_y);
    }
}
