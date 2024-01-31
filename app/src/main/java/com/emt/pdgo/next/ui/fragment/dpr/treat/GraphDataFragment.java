package com.emt.pdgo.next.ui.fragment.dpr.treat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.PidBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.ui.activity.dpr.DprExamineActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.DynamicLineChartManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pdp.rmmit.pdp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GraphDataFragment extends BaseFragment {

    @BindView(R.id.const_p1Ll)
    LinearLayout const_p1Ll;
    @BindView(R.id.const_p1)
    TextView const_p1;

    @BindView(R.id.const_i1Ll)
    LinearLayout const_i1Ll;
    @BindView(R.id.const_i1)
    TextView const_i1;

    @BindView(R.id.const_d1Ll)
    LinearLayout const_d1Ll;
    @BindView(R.id.const_d1)
    TextView const_d1;

    @BindView(R.id.const_p2Ll)
    LinearLayout const_p2Ll;
    @BindView(R.id.const_p2)
    TextView const_p2;

    @BindView(R.id.const_i2Ll)
    LinearLayout const_i2Ll;
    @BindView(R.id.const_i2)
    TextView const_i2;

    @BindView(R.id.const_d2Ll)
    LinearLayout const_d2Ll;
    @BindView(R.id.const_d2)
    TextView const_d2;

    @BindView(R.id.saturate_bwLl)
    LinearLayout saturate_bwLl;
    @BindView(R.id.saturate_bw)
    TextView saturate_bw;

    @BindView(R.id.saturate_bw2Ll)
    LinearLayout saturate_bw2Ll;
    @BindView(R.id.saturate_bw2)
    TextView saturate_bw2;

    @BindView(R.id.pidLayout)
    LinearLayout pidLayout;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @BindView(R.id.barChat)
    BarChart barChart;
    private List<BarEntry> barEntries = new ArrayList<>();

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.bezierLineChart)
    LineChart bezierLineChart;

    private DynamicLineChartManager chartManager;

    private DynamicLineChartManager lineChartManager;
//    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> name; //折线名字集合
    private List<Integer> color;//折线颜色集合

    private List<String> bName; //折线名字集合
    private List<Integer> bColor;//折线颜色集合

    private PidBean pidBean;

    private DprExamineActivity activity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity =  (DprExamineActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        RxBus.get().register(this);
        initView();
//        Log.e("GraphDataFragment", "GraphDataFragment onCreateView");
        return view;
    }

    public void initView() {
        initPidBean();
//        initBarchart();
        initLineChat();
        initBezierChat();
        click();
    }

    private void initPidBean() {
        pidBean = PdproHelper.getInstance().pidBean();
        const_p1.setText(String.valueOf(pidBean.const_p1));
        const_i1.setText(String.valueOf(pidBean.const_i1));
        const_d1.setText(String.valueOf(pidBean.const_d1));
        const_p2.setText(String.valueOf(pidBean.const_p2));
        const_i2.setText(String.valueOf(pidBean.const_i2));
        const_d2.setText(String.valueOf(pidBean.const_d2));
        saturate_bw.setText(String.valueOf(pidBean.saturate_bw1));
        saturate_bw2.setText(String.valueOf(pidBean.saturate_bw2));
    }

    private void initLineChat() {
        name = new ArrayList<>();
        name.add("灌注");
        name.add("引流");
//        name.add("总灌注");
//        name.add("总引流");
//        name.add("灌注位置");
//        name.add("引流位置");
//        name.add("上位称");
//        name.add("下位称");

        color = new ArrayList<>();

        color.add(Color.BLUE);
        color.add(Color.MAGENTA);

//        color.add(Color.YELLOW);
//        color.add(Color.GREEN);

//        color.add(Color.LTGRAY);
//        color.add(Color.DKGRAY);

        chartManager = new DynamicLineChartManager(lineChart, name, color);
    }

    public void addEntry(List<Integer> numbers) {
        chartManager.addEntry(numbers);
    }

    private void initBezierChat() {
        bName = new ArrayList<>();
        bName.add("上位称");
        bName.add("下位称");

        bColor = new ArrayList<>();
        bColor.add(Color.BLUE);
        bColor.add(Color.MAGENTA);
        lineChartManager = new DynamicLineChartManager(bezierLineChart, bName, bColor);
    }

    public void addBarEntry(List<Integer> numbers) {
        lineChartManager.addEntry(numbers);
    }

    private void click() {
        const_p1Ll.setOnClickListener(v -> {
            alertNumberBoardDialog(const_p1.getText().toString(),"const_p1");
        });
        const_i1Ll.setOnClickListener(v -> {
            alertNumberBoardDialog(const_i1.getText().toString(),"const_i1");
        });
        const_d1Ll.setOnClickListener(v -> {
            alertNumberBoardDialog(const_d1.getText().toString(),"const_d1");
        });
        const_p2Ll.setOnClickListener(v -> {
            alertNumberBoardDialog(const_p2.getText().toString(),"const_p2");
        });
        const_i2Ll.setOnClickListener(v -> {
            alertNumberBoardDialog(const_i2.getText().toString(),"const_i2");
        });
        const_d2Ll.setOnClickListener(v -> {
            alertNumberBoardDialog(const_d2.getText().toString(),"const_d2");
        });
        saturate_bwLl.setOnClickListener(v -> {
            alertNumberBoardDialog(saturate_bw.getText().toString(),"saturate_bw");
        });
        saturate_bw2Ll.setOnClickListener(v -> {
            alertNumberBoardDialog(saturate_bw2.getText().toString(),"saturate_bw2");
        });
        btnConfirm.setOnClickListener(v -> {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.PID_BEAN, pidBean);
            activity.sendToMainBoard(CommandDataHelper.getInstance().pidParam(PdproHelper.getInstance().pidBean(), true));
        });
    }


    private void initBarchart() {
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.setScaleEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDrawBorders(false);
        //设置动画效果
        barChart.animateY(1000, Easing.EasingOption.Linear);
        barChart.animateX(1000, Easing.EasingOption.Linear);
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(true);

        // 获取X轴
        XAxis xAxis = barChart.getXAxis();
        // 显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(false); // 垂直网格线
//        xAxis.setTextSize(15f); // 坐标字体大小
//        xAxis.setAxisLineColor(Color.TRANSPARENT); // 坐标轴颜色
//        xAxis.setAxisLineWidth(2f); // 坐标轴宽度
        xAxis.setDrawGridLines(false);
//        xAxis.setEnabled(false);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//
//                return null;
//            }
//        });
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        });
        YAxis yAxis = barChart.getAxisLeft();
//        yAxis.setAxisMinimum(-20f);
//        yAxis.setStartAtZero(false);
//        yAxis.setEnabled(false);
//        yAxis.setDrawZeroLine(true);

        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setFormSize(8f);
//        l.setFormToTextSpace(4f);
//        l.setXEntrySpace(6f);
        setData(0,new float[]{0});
    }
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
    private List<String> timeList = new ArrayList<>(); //存储x轴的时间
    public void setData(float x, float[] y) {
//        for (int i = -15; i < 15 + 1; i++) {
//            float v = (10 + i);
//            float val1 = (float) (Math.random() * v) + v / 3;
//            float val2 = (float) (Math.random() * v) + v / 3;
//            float val3 = (float) (Math.random() * v) + v / 3;
//            float val4 = (float) (Math.random() * v) + v / 3;
//            float val5 = (float) (Math.random() * v) + v / 3;
//            float val6 = (float) (Math.random() * v) + v / 3;
//                            barEntries.add(new BarEntry(i, new float[]{val1, val2, val3,
//                                    -val4, -val5, -val6
//                            }));
////            graphDataFragment.setData(i, new float[]{val1, val2, val3});
//        }
        timeList.add(df.format(System.currentTimeMillis()));
        barEntries.add(new BarEntry(x, y));

        BarDataSet set;
        if (barChart.getBarData() != null &&
                barChart.getBarData().getDataSetCount() > 0) {
            set = (BarDataSet) barChart.getBarData().getDataSetByIndex(0);
            set.setValues(barEntries);
            set.setDrawValues(true);
            barChart.getBarData().notifyDataChanged();
            barChart.notifyDataSetChanged();
            barChart.setVisibleXRangeMaximum(50);
        } else {
            set = new BarDataSet(barEntries, "");
            // Color.BLUE, Color.YELLOW, Color.parseColor("#F2994A"),Color.GREEN
            set.setColors(Color.BLUE, Color.YELLOW, Color.parseColor("#F2994A"),Color.GREEN);
            set.setStackLabels(new String[]{"引流", "灌注", "灌注液浓度变化","超滤"});
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            BarData data = new BarData(dataSets);
//            data.setValueFormatter(new MyAxisValueFormatter(labels));
            data.setValueTextColor(Color.BLACK);
            barChart.setData(data);
        }
        barChart.setFitBars(true);
        barChart.invalidate();
    }

    private int[] getColors() {
        int size = 3;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[size];
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, colors.length);
        return colors;
    }

    public static class MyAxisValueFormatter implements IValueFormatter {

        private final List<String> labels;

        /**
         * @param labels 要显示的标签字符数组
         */
        public MyAxisValueFormatter(List<String> labels) {
            this.labels = labels;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return labels.get((int) value % labels.size());
        }
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, false);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case "const_p1":
                        pidBean.const_p1 = Float.parseFloat(result);
                        const_p1.setText(String.valueOf(pidBean.const_p1));
                        break;
                    case "const_i1":
                        pidBean.const_i1 = Float.parseFloat(result);
                        const_i1.setText(String.valueOf(pidBean.const_i1));
                        break;
                    case "const_d1":
                        pidBean.const_d1 = Float.parseFloat(result);
                        const_d1.setText(String.valueOf(pidBean.const_d1));
                        break;
                    case "const_p2":
                        pidBean.const_p2 = Float.parseFloat(result);
                        const_p2.setText(String.valueOf(pidBean.const_p2));
                        break;
                    case "const_i2":
                        pidBean.const_i2 = Float.parseFloat(result);
                        const_i2.setText(String.valueOf(pidBean.const_i2));
                        break;
                    case "const_d2":
                        pidBean.const_d2 = Float.parseFloat(result);
                        const_d2.setText(String.valueOf(pidBean.const_d2));
                        break;
                    case "saturate_bw":
                        pidBean.saturate_bw1 = Float.parseFloat(result);
                        saturate_bw.setText(String.valueOf(pidBean.saturate_bw1));
                        break;
                    case "saturate_bw2":
                        pidBean.saturate_bw2 = Float.parseFloat(result);
                        saturate_bw2.setText(String.valueOf(pidBean.saturate_bw2));
                        break;
                }
            }
        });
    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void notifyByThemeChanged() {

    }
    
}