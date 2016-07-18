package ru.tinkoff.cake.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ru.tinkoff.cake.fragment.CakeDialog;
import ru.tinkoff.cake.R;
import ru.tinkoff.cake.data.Score;
import ru.tinkoff.cake.utils.PreferencesHelper;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_SCORE = "EXTRA_SCORE";
    private static final int TOP_SCORE_TABLE_SIZE = 5;
    public static void start(Context context, int playerScore) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(EXTRA_SCORE, playerScore);
        context.startActivity(intent);
    }

    private LinearLayout scoreTable;

    private EditText nameEditText;
    private TextView scoreTextView;
    private Button repeatButton;
    private Button saveButton;

    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        scoreTable = (LinearLayout) findViewById(R.id.result_ll_top);
        scoreTextView = (TextView) findViewById(R.id.result_tv_score);
        nameEditText = (EditText) findViewById(R.id.result_et_name);
        saveButton = (Button) findViewById(R.id.results_btn_save);
        repeatButton = (Button) findViewById(R.id.results_btn_repeat);

        preferencesHelper = new PreferencesHelper(this);

        String lastUsedName = preferencesHelper.getLastUsedName();
        if (lastUsedName != null) {
            nameEditText.setText(lastUsedName);

        }

        initScoreTable();

        int score = getIntent().getIntExtra(EXTRA_SCORE, 0);
        scoreTextView.setText(getString(R.string.label_score, score));
        saveButton.setOnClickListener(this);
        repeatButton.setOnClickListener(this);
    }

    private void initScoreTable() {
        List<Score> scoreList = preferencesHelper.getTopScores();
        Collections.sort(scoreList);
        int size = scoreList.size();
        int limit = size > TOP_SCORE_TABLE_SIZE ? (size - TOP_SCORE_TABLE_SIZE) : 0;
        String scoreTableFormat = getString(R.string.format_score_table);
        int placeCount = 1;
        scoreTable.removeAllViews();
        for (int i = size - 1; i >= limit; i--) {
            Score score = scoreList.get(i);
            TextView textView = new TextView(this);
            textView.setTextColor(Color.WHITE);
            textView.setText(String.format(scoreTableFormat, placeCount, score.getName(), score.getScore()));
            scoreTable.addView(textView);
            placeCount++;
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == saveButton.getId()) {
            saveButton.setOnClickListener(null);
            tryToSaveScore();
            initScoreTable();
            saveButton.animate().translationY(saveButton.getHeight() * 3);
            repeatButton.animate().translationY(0);
        } else if (viewId == repeatButton.getId()) {
            finish();
            MainActivity.start(this);
        }
    }

    private void tryToSaveScore() {
        List<Score> scoreList = preferencesHelper.getTopScores();
        int score = getIntent().getIntExtra(EXTRA_SCORE, -1);
        String name = nameEditText.getText().toString().trim();

        if (score == -1 || TextUtils.isEmpty(name)) {
            showErrorDialog();
        } else {
            scoreList.add(new Score(name, score));
            preferencesHelper.saveTopScores(scoreList);
            preferencesHelper.saveLastUsedName(name);
        }
    }

    private void showErrorDialog() {
        String title = getString(R.string.title_err_dialog);
        String message = getString(R.string.message_err_save_score);
        DialogFragment dialog = CakeDialog.newInstance(title, message);
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
