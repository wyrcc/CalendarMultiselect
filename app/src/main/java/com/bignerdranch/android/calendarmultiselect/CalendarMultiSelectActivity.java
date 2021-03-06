package com.bignerdranch.android.calendarmultiselect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarMultiSelectActivity extends AppCompatActivity {

    public Context mContext;

    public final String TAG = "日历";

    // 选择年
    public String mXuanYear = "2017";
    // 选择月
    public String mXuanMonth = "02";

    // 开始日期
    public String mKaiSiRiQi = "01";
    // 结束日期
    public String mJieShuRiQi = "31";

    // 月初周几号
    public int mZhouJi = 0;

    // 月末周几
    public int mMoZhouJi = 0;

    // 日历父节点
    public LinearLayout mLinear_ri_li;

    // 选择年
    public TextView mTextView_nian;
    // 年数据
    public String[] stringsNian = new String[2];

    // 选择月
    public TextView mTextView_yue;
    // 月数据
    public String[] stringsyue = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};

    // 当天
    public TextView mTextView_jing_ri;

    // 选择器
    private AlertDialog alertDialog1;

    // 1 编辑模式 0 显示模式
    public int mMoShi = CalendarConfig.mMoShi;

    // 已选择数据
    List<DayColor> myiXuanZheData = CalendarConfig.mYiXuanZheData;

    // 选择日期返回标识
    public static final String mFanHuiBiao = "data";

    // 返回按钮
    public Button mFan_hui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 组件初始化
        ViewInit();

        // 值操作
        value();

        // 组件操作
        // 组件操作, 操作
        ZhuJianCaoZhuo();
    }

    /**
     * 返回监听事件
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {//按键的按下事件
                fanhui();
            } else if (event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0) {//按键的抬起事件

            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 返回数据准备
     */
    public void fanhui(){
        Intent intent = new Intent();

        JSONArray json = new JSONArray();

        for(DayColor dayColor : myiXuanZheData){
            json.put(dayColor.getDay());
        }

        intent.putExtra(mFanHuiBiao,json.toString());
        setResult(MainActivity.REQUEST_PHOTO,intent);
    }

    /**
     * 组件组件初始化
     */
    public void ViewInit(){
        mLinear_ri_li = (LinearLayout)findViewById(R.id.linear_ri_li);
        // 年
        mTextView_nian = (TextView)findViewById(R.id.textView_nian);
        // 月
        mTextView_yue = (TextView)findViewById(R.id.textView_yue);
        // 当前日
        mTextView_jing_ri = (TextView)findViewById(R.id.textView_jing_ri);
        // 返回按钮
        mFan_hui = (Button)findViewById(R.id.fan_hui);
    }

    /**
     * 组件操作
     */
    public void ZhuJianCaoZhuo(){
        // 选择年
        mTextView_nian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(stringsNian, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        mXuanYear = stringsNian[index];
                        // 视图创建,选择年月显示
                        ViewShowNianYue();
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });
        // 选择月
        mTextView_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(stringsyue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        mXuanMonth = stringsyue[index];
                        // 视图创建,选择年月显示
                        ViewShowNianYue();
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();

            }
        });

        // 今日
        mTextView_jing_ri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value();
            }
        });

        // 返回按钮触发事件
        mFan_hui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fanhui();
                finish();
            }
        });

    }

    /**
     * 设置年 月显示
     */
    public void nianYueShow(String nianStr,String yueStr){
        mTextView_nian.setText(nianStr+"年");
        mTextView_yue.setText(yueStr+"月");
    }

    /**
     * 设置默认年月
     */
    public void setMoRenNianYue(){

        // 开始日期
        mKaiSiRiQi = getFirstDayOfMonth(Integer.valueOf(mXuanYear),Integer.valueOf(mXuanMonth));
        // 结束日期
        mJieShuRiQi = getLastDayOfMonth(Integer.valueOf(mXuanYear),Integer.valueOf(mXuanMonth));
        // 月初周几
        mZhouJi = getWeekOfDate(mXuanYear+"-"+mXuanMonth+"-"+mKaiSiRiQi);
        // 月末周几
        mMoZhouJi = getWeekOfDate(mXuanYear+"-"+mXuanMonth+"-"+mJieShuRiQi);
    }

    /**
     * 值操作
     */
    public void value(){
        // 单选模式
        if(CalendarConfig.mDanXuanMoShi == 1){
            Toast.makeText(mContext,"单选模式,只会取第一次选择", Toast.LENGTH_SHORT).show();
        }
        // 按钮样式
        mFan_hui.setTextSize(CalendarConfig.mButtonFontSize);
        mFan_hui.setText(CalendarConfig.mButtonText);
        mFan_hui.setTextColor(getResources().getColor(CalendarConfig.mButtonTextColor));
        mFan_hui.setBackground(getResources().getDrawable(CalendarConfig.mButtonBackground));
        // 当前年赋值
        stringsNian[0] = getYearMonth(1);
        // 下一年赋值
        stringsNian[1] = String.valueOf(Integer.valueOf(getYearMonth(1))+1);
        // 当前年
        mXuanYear = getYearMonth(1);
        // 当前月
        mXuanMonth = getYearMonth(2);
        // 设置默认年月
        setMoRenNianYue();
        // 视图创建,选择年月显示
        ViewShowNianYue();
    }

    /**
     * 视图创建,选择年月显示
     */
    public void ViewShowNianYue(){
        // 设置年月
        setMoRenNianYue();
        // 显示当前年,月
        nianYueShow(mXuanYear,mXuanMonth);

        // 创建布局
        CreateBuJu();
    }

    // 创建日历
    public void CreateBuJu(){
        // 清空内容
        mLinear_ri_li.removeAllViews();

        // 最后一天
        int jieshu = Integer.valueOf(mJieShuRiQi);
        // 数组长度
        int arrnum = jieshu+mZhouJi+6-mMoZhouJi;
        // 构造显示数组
        String[] strings = new String[arrnum];
        // 月初评接
        for(int i = 0;i<=mZhouJi;i++){
            strings[i] = "";
        }
        // 本身显示日期
        for(int i = 1;i<=jieshu;i++){
            strings[(mZhouJi+i)-1] = ""+i;
        }
        // 月末评接
        for(int i = 1;i<=6-mMoZhouJi;i++){
            strings[jieshu+mZhouJi-1+i] = "";
        }
        CreateView(strings);
    }

    /**
     * 创建视图
     * @param strings
     */
    public void CreateView(String[] strings){
        int num = 0;

        LinearLayout waiBuZuJian = CreateLinearLayout(1);

        for(int i = 0;i<strings.length;i++){
            if(strings[i] != null){
                LinearLayout neiBuZuJian = CreateLinearLayout(2);
//                TextView textView = CreateTextView(strings[i],0,0,0);
                //
                TextView textView = isCreateTextDanXuan(strings[i]);
                // 查看是否创建背景色
                String[] stringsc = ValueIs(mXuanYear+"-"+mXuanMonth+"-"+strings[i]);
                if(stringsc[0].equals("true")){
                    DayColor dayColor = myiXuanZheData.get(Integer.valueOf(stringsc[1]));
//                    Log.i("巡店",strings[i]);
                    textView = CreateTextView(strings[i],1,dayColor.getFontColor(),dayColor.getColor());
                }

                neiBuZuJian.addView(textView);
                waiBuZuJian.addView(neiBuZuJian);
                num++;
                if(num == 7){
                    mLinear_ri_li.addView(waiBuZuJian);
                    waiBuZuJian = CreateLinearLayout(1);
                    num = 0;
                }else if((strings.length-1) == i){
                    mLinear_ri_li.addView(waiBuZuJian);
                    waiBuZuJian = CreateLinearLayout(1);
                }
            }
        }
    }

    /**
     * 查看天是创建正常还是提示,创建每天textview
     * @param day 那一天
     * @return
     */
    public TextView isCreateTextDanXuan(String day){
        String RiQi = mXuanYear+"-"+mXuanMonth+"-"+day;
        Boolean isC = false;
        if(CalendarConfig.mZhouJiBuKeXuan.length > 0){
            for(int i = 0;i<CalendarConfig.mZhouJiBuKeXuan.length;i++){
                isC = false;
//                Log.i("巡店",CalendarConfig.mZhouJiBuKeXuan[i]);
                if(CalendarConfig.mZhouJiBuKeXuan[i].equals("")){

                }else{
                    if(CalendarConfig.mZhouJiBuKeXuan[i].equals(getCurrentWeekOfMonth(RiQi))){
                        isC = true;
                        break;
                    }
                }

            }
        }

        TextView textView = new TextView(mContext);
        // 周几是否可选
        if(isC){
            if(day.length() > 0) {
                textView = CreateTextViewString(day, CalendarConfig.mZhouJiBuKeXuanTiShi, R.color.baiseCalendar, R.drawable.ri_qi_background_huise);
            }
        }else{
            if(day.length() > 0) {
                textView = CreateTextView(day,0,0,0);
            }

        }

        // 小于当前日期的是否可选
        // 今日日期
        int JinRiYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
        int JinRiMonth = Integer.valueOf(new SimpleDateFormat("MM").format(new Date()));
        int JinRiDay = Integer.valueOf(new SimpleDateFormat("d").format(new Date()));

//        Log.i("巡店",mXuanYear+""+mXuanMonth+""+day+"|"+JinRiYear+""+JinRiMonth+""+JinRiDay);
        if(CalendarConfig.mYiGuoBuKeXuan == 1  && day.length() > 0){
//            Log.i("巡店",Integer.valueOf(mXuanYear)+"|"+JinRiYear+"-"+Integer.valueOf(mXuanMonth)+"|"+JinRiMonth+"-"+Integer.valueOf(day)+"|"+JinRiDay);
            if(Integer.valueOf(mXuanYear) <= JinRiYear && Integer.valueOf(mXuanMonth) <= JinRiMonth && Integer.valueOf(day) < JinRiDay){
                textView = CreateTextViewString(day,CalendarConfig.mYiGuoBuKeXuanTiShi,R.color.baiseCalendar,R.drawable.ri_qi_background_huise);
            }
        }




        return textView;
        // 查看当前日期周几
    }

    /**
     * @author jerry.chen 2017-10-12
     * @param dateStr
     * @return 获取当前是星期几
     */
    public String getCurrentWeekOfMonth(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            String[] day_of_week = {"周日","周一","周二","周三","周四","周五","周六"};
            c.setTime(format.parse(dateStr));
            int dayForWeek = 0;

            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            return day_of_week[dayForWeek];

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return "";
    }

    public String[] ValueIs(String string){
        String[] strings = new String[2];
        strings[0] = "false";
        strings[1] = "0";
        for (int i = 0;i<myiXuanZheData.size();i++){
            if(myiXuanZheData.get(i).getDay().equals(string)){
                strings[0] = "true";
                strings[1] = ""+i;
            }
        }
        return strings;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param string
     * @return 当前日期是星期几
     */
    public int getWeekOfDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int[] weekDays = {0,1,2,3,4,5,6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取指定的年的月的第一天
     * @param year
     * @param month
     * @return
     */
    public String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat( "dd").format(cal.getTime());
    }

    /**
     * 获取指定的年的月的最后一天
     * @param year
     * @param month
     * @return
     */
    public String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat( "dd").format(cal.getTime());
    }

    /**
     * 返回年 月
     * @param is 1 返回年 2返回月 3返回日
     * @return
     */
    public String getYearMonth(int is){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));
        if(is == 1){
            return new SimpleDateFormat("yyyy").format(cal.getTime());
        }else if(is == 2){
            return new SimpleDateFormat("MM").format(cal.getTime());
        }else if(is == 3){
            return new SimpleDateFormat("d").format(new Date());
        }

        return null;
    }

    /**
     * 创建textview
     * @param string 显示内容
     * @param leix 1 选中状态 0 为选中状态
     * @param color 标题颜色
     * @param backgr 背景资源
     * @return
     */
    public TextView CreateTextView(String string, int leix, int color,int backgr){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(68,68);

        textView.setLayoutParams(layoutParam);
        textView.setText(string);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(5,5,5,5);
        textView.setTextColor(getResources().getColor(R.color.huise6Calendar));

        if(leix == 1 && string != ""){
            textView.setTextColor(getResources().getColor(color));
            textView.setBackground(getResources().getDrawable(backgr));
        }

        if(mMoShi == 1 && string != ""){

            final String XinShiStr = string;
            final TextView textView1 = textView;
            textView.setOnClickListener(new View.OnClickListener() {
                // 是否点击 1 点击 0 未点击
                int isDianJi = 0;
                @Override
                public void onClick(View view) {
                    String day = mXuanYear+"-"+mXuanMonth+"-"+XinShiStr;
                    String[] strings = ValueIs(day);
                    if(strings[0].equals("true")){
                        textView1.setTextColor(getResources().getColor(R.color.huise6Calendar));
                        textView1.setBackground(getResources().getDrawable(R.drawable.ri_qi_background1));
                        // 删除存储的数据
                        int i = Integer.valueOf(strings[1]);
                        myiXuanZheData.remove(i);
                    }else{
                        // 单选模式只能选择一次时间
                        if(CalendarConfig.mDanXuanMoShi == 1 && myiXuanZheData.size() == 1){
                            Toast.makeText(mContext,"单选模式,只能选择一次", Toast.LENGTH_SHORT).show();
                        }else{
                            textView1.setTextColor(getResources().getColor(CalendarConfig.mMoRenZiTiSe));
                            textView1.setBackground(getResources().getDrawable(CalendarConfig.mMoRenBeiJingSe));
                            // 存储选择
                            DayColor dayColor = new DayColor();
                            dayColor.setDay(day);
                            dayColor.setColor(CalendarConfig.mMoRenBeiJingSe);
                            dayColor.setFontColor(CalendarConfig.mMoRenZiTiSe);
                            myiXuanZheData.add(dayColor);
                        }

                    }
                }
            });

        }


        return textView;
    }

    /**
     * 创建textview
     * @param string 显示内容
     * @param TiShi 提示文字
     * @param color 标题颜色
     * @param backgr 背景资源
     * @return
     */
    public TextView CreateTextViewString(String string,final String TiShi, int color,int backgr){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(68,68);

        textView.setLayoutParams(layoutParam);
        textView.setText(string);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(5,5,5,5);
        textView.setTextColor(getResources().getColor(R.color.huise6Calendar));

        textView.setTextColor(getResources().getColor(color));
        textView.setBackground(getResources().getDrawable(backgr));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,TiShi, Toast.LENGTH_SHORT).show();
            }
        });


        return textView;
    }

    /**
     * 创建布局
     * @param leix 1外部行布局  2内部布局 UIBackgroundModes
     * @return
     */
    public LinearLayout CreateLinearLayout(int leix){
        LinearLayout linearLayout = new LinearLayout(mContext);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(leix == 2){
            layoutParam = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
        }


        linearLayout.setLayoutParams(layoutParam);

        if(leix == 1){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0,30,0,30);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.calendar_bottom_border));
        }else if(leix == 2){
            linearLayout.setGravity(Gravity.CENTER);
        }

        return linearLayout;
    }
}
