package com.cpvcourse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BaseFragment extends Fragment {
    private SubsamplingScaleImageView imageChapter;
    private TextView chapterNameTextView;
    private TextView exam_dateTV, exam_conditionTV, exam_timeTV, exam_printTV, exam_inquiryTV, exam_receiveTV;

    public static BaseFragment newInstance(int index) {
        Bundle args = new Bundle();
        BaseFragment fragment = new BaseFragment();

        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);  //注册

        View view = null;
        int i = getArguments().getInt("index");
        if (i == 0) {
            view = inflater.inflate(R.layout.fragment_home, null);

            exam_dateTV = (TextView) view.findViewById(R.id.textview_exam_date);
            exam_conditionTV = (TextView) view.findViewById(R.id.textview_exam_condition);
            exam_timeTV = (TextView) view.findViewById(R.id.textview_exam_time);
            exam_printTV = (TextView) view.findViewById(R.id.textview_exam_print);
            exam_inquiryTV = (TextView) view.findViewById(R.id.textview_exam_inquiry);
            exam_receiveTV = (TextView) view.findViewById(R.id.textview_exam_receive);

            exam_dateTV.setMovementMethod(LinkMovementMethod.getInstance());
            exam_conditionTV.setMovementMethod(LinkMovementMethod.getInstance());
            exam_timeTV.setMovementMethod(LinkMovementMethod.getInstance());
            exam_printTV.setMovementMethod(LinkMovementMethod.getInstance());
            exam_inquiryTV.setMovementMethod(LinkMovementMethod.getInstance());
            exam_receiveTV.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            view = inflater.inflate(R.layout.fragment_course, null);
            chapterNameTextView = (TextView) view.findViewById(R.id.textview_chapter_name);
            imageChapter = (SubsamplingScaleImageView) view.findViewById(R.id.img_chapter);
        }
        return view;
    }

    // 接收函数一
    @Subscribe
    public void onEvent(EventUtil event) {
        int i = event.getCourseInt();
        int j = event.getChapterInt();
        String chapterName = CourseConstant.CHAPTERS[i][j];
        chapterNameTextView.setText(chapterName);
        imageChapter.setImage(ImageSource.asset(CourseConstant.CHAPTERS_URL[i][j]));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);//取消注册
    }
}
