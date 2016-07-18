package ru.tinkoff.cake.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.tinkoff.cake.R;
import ru.tinkoff.cake.fragment.ModelFragment;

public class ChoosePlayerModelActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MODEL_POSITION = "EXTRA_MODEL_POSITION";

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        viewPager = (ViewPager) findViewById(R.id.choose_view_pager);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        ChooseModelAdapter adapter = new ChooseModelAdapter(supportFragmentManager);
        viewPager.setAdapter(adapter);

        Button chooseButton = (Button) findViewById(R.id.choose_btn);

        chooseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = viewPager.getCurrentItem();
        Intent data = new Intent();
        data.putExtra(EXTRA_MODEL_POSITION, position);
        setResult(RESULT_OK, data);
        finish();
    }


    static class ChooseModelAdapter extends FragmentPagerAdapter {

        public ChooseModelAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return ModelFragment.MODELS.length;
        }

        @Override
        public Fragment getItem(int position) {
            return ModelFragment.newInstance(position);
        }
    }
}
