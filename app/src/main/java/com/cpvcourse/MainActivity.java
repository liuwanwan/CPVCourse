package com.cpvcourse;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.cpvcourse.adapter.IndicatorExpandableListAdapter;
import org.greenrobot.eventbus.EventBus;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private int viewPagerSelected = 0;
    private int courseInt = 0, chapterInt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initViewPager();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_knowledge:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_exercise:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
                viewPagerSelected = position;
                refreshMenu();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
    }

    private void refreshMenu() {
        supportInvalidateOptionsMenu();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(BaseFragment.newInstance(0));
        adapter.addFragment(BaseFragment.newInstance(1));
        adapter.addFragment(BaseFragment.newInstance(2));
        viewPager.setAdapter(adapter);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getDate());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_contents:
                        showPopContents();
                        break;
                    case R.id.menu_settings:
                        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_help:
                        showHelpDialog();
                        break;
                    case R.id.menu_specification:
                        showSecificationDialog();
                        break;
                }
                return true;
            }
        });
    }

    private void showPopContents() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_content, (ViewGroup) findViewById(R.id.content));

        final ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandablelistview);
        final IndicatorExpandableListAdapter adapter = new IndicatorExpandableListAdapter(CourseConstant.BOOKS, CourseConstant.CHAPTERS);
        listView.setAdapter(adapter);
        // 清除默认的 Indicator
        listView.setGroupIndicator(null);
        //listView.setVerticalScrollBarEnabled(false);
        //  设置分组项的点击监听事件
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                adapter.setIndicatorState(groupPosition, groupExpanded);
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                courseInt = groupPosition;
                chapterInt = childPosition;
                Toast.makeText(MainActivity.this, CourseConstant.CHAPTERS[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("课程章节");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                EventUtil eventUtil = new EventUtil();
                eventUtil.setCourseInt(courseInt);
                eventUtil.setChapterInt(chapterInt);
                EventBus.getDefault().post(eventUtil);
            }
        });
        builder.setView(v);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.show();

    }
    public static String getDate(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
       String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String  mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String  mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear + "年" + mMonth + "月" + mDay+"日"+"/星期"+mWay;
    }
    private void showHelpDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_dialog_help, (ViewGroup) findViewById(R.id.dialog_help));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("使用帮助");
        builder.setPositiveButton("确定", null);
        builder.setView(dialog);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.show();
    }

    private void showSecificationDialog() {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("软件说明");
        builder.setMessage("这是 android.support.v7.app.AlertDialog 中的样式");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId = 0;
        switch (viewPagerSelected) {
            case 0:
                menuId = R.menu.menu_home;
                break;
            case 1:
                menuId = R.menu.menu_course;
                break;
            case 2:
                menuId = R.menu.menu_course;
                break;
        }
        setIconsVisible(menu, true);
        getMenuInflater().inflate(menuId, menu);
        return true;
    }

    // 解决menu不显示图标问题
    private void setIconsVisible(Menu menu, boolean flag) {
        //判断menu是否为空
        if (menu != null) {
            try {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
