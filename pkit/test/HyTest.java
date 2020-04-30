import java.util.Arrays;

import com.hyjavacharts.chart.Highchart;
import com.hyjavacharts.model.highcharts.ChartOptions;
import com.hyjavacharts.model.highcharts.YAxis;
import com.hyjavacharts.model.highcharts.constants.HorizontalAlign;
import com.hyjavacharts.model.highcharts.constants.Layout;
import com.hyjavacharts.model.highcharts.constants.VerticalAlign;
import com.hyjavacharts.model.highcharts.series.SeriesLine;

public class HyTest{
    private static final long serialVersionUID = 8451224834547659420L;

    public static void main(String[] args) {
        Highchart hc = new HyTest().configure();
        String globalOptionsJs = hc.globalOptionsToJs();
        String chartOptionsJs = hc.chartOptionsToJs();
        System.out.println(chartOptionsJs);
    }

    public Highchart configure() {
        Highchart highChart = new Highchart();
        ChartOptions chartOptions = highChart.getChartOptions();

        chartOptions.getTitle().setText("Solar Employment Growth by Sector, 2010-2016");
        chartOptions.getSubtitle().setText("Source: thesolarfoundation.com");

        YAxis yAxi = new YAxis();
        yAxi.getTitle().setText("Number of Employees");
        chartOptions.getYAxis().add(yAxi);

        chartOptions.getLegend().setLayout(Layout.VERTICAL).setAlign(HorizontalAlign.RIGHT).setVerticalAlign(VerticalAlign.MIDDLE);
        chartOptions.getPlotOptions()
                .getSeries()
                .setPointStart(2010)
                .getLabel().setConnectorAllowed(false);

        chartOptions.getResponsive().getRulesSingle()
                .getCondition().setMaxWidth(500);
        chartOptions.getResponsive().getRulesSingle()
                .getChartOptions().getLegend()
                .setLayout(Layout.HORIZONTAL).setAlign(HorizontalAlign.CENTER).setVerticalAlign(VerticalAlign.BOTTOM);

        SeriesLine seriesLine = new SeriesLine();
        seriesLine.setName("Installation");
        seriesLine.setDataAsArrayNumber(Arrays.asList(43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175));
        chartOptions.getSeries().add(seriesLine);

        seriesLine = new SeriesLine();
        seriesLine.setName("Manufacturing");
        seriesLine.setDataAsArrayNumber(Arrays.asList(24916, 24064, 29742, 29851, 32490, 30282, 38121, 40434));
        chartOptions.getSeries().add(seriesLine);

        seriesLine = new SeriesLine();
        seriesLine.setName("Sales & Distribution");
        seriesLine.setDataAsArrayNumber(Arrays.asList(11744, 17722, 16005, 19771, 20185, 24377, 32147, 39387));
        chartOptions.getSeries().add(seriesLine);

        seriesLine = new SeriesLine();
        seriesLine.setName("Project Development");
        seriesLine.setDataAsArrayNumber(Arrays.asList(null, null, 7988, 12169, 15112, 22452, 34400, 34227));
        chartOptions.getSeries().add(seriesLine);

        seriesLine = new SeriesLine();
        seriesLine.setName("Other");
        seriesLine.setDataAsArrayNumber(Arrays.asList(12908, 5948, 8105, 11248, 8989, 11816, 18274, 18111));
        chartOptions.getSeries().add(seriesLine);

        return highChart;
    }

}