package ru.tinkoff.cake.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import ru.tinkoff.cake.LicenceManager;
import ru.tinkoff.cake.R;
import ru.tinkoff.cake.ValueAnimatorCompatManager;
import ru.tinkoff.cake.fragment.ModelFragment;

// TODO дополнительное задание. Сейчас игра начинается сразу при запуске MainActivity,
// нужно добавить кнопку, по нажатию на которую игра запустится
// подсказка: вам может пригодится тот же самый код, который ставит игру на паузу
public class MainActivity extends AppCompatActivity {

    // TODO эта константа может пригодиться...
    private static final int CHOOSE_MODEL_REQUEST_CODE = 2323;

    public static void start(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    private static final String KEY_PLAYER_ANIMATOR = "KEY_PLAYER_ANIMATOR";
    private static final String KEY_DANGER_ANIMATOR = "KEY_DANGER_ANIMATOR";
    private static final String KEY_JUMP_ANIMATOR = "KEY_JUMP_ANIMATOR";

    private static final long ANIMATION_DURATION_PLANE = 750;

    private static final int TRANSLATION_Y_DIFF = 25;

    private static final float JUMP_VALUE = 300;

    private TextView scoreTextView;
    private View root;
    private ImageView player;
    private View danger;

    private Button playPauseButton;
    private Button settingsButton;

    private ObjectAnimator playerAnimator;
    private ObjectAnimator dangerAnimator;

    private ObjectAnimator jumpAnimator;

    private CollisionDetector collisionDetector;

    private View.OnClickListener playerJumpClickListener;
    private View.OnClickListener pauseClickListener;
    private View.OnClickListener playClickListener;

    private ValueAnimatorCompatManager animatorManager;

    private int currentGameScore;

    private int playerStartPosY;

    private boolean isJumpAnimatorWasActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseButton = (Button) findViewById(R.id.main_btn_pause);
        settingsButton = (Button) findViewById(R.id.main_btn_settings);

        animatorManager = new ValueAnimatorCompatManager();
        LicenceManager licenceManager = new LicenceManager(this);

        initClickListeners();

        if (licenceManager.isUserCanPlay()) {
            initGame();
        } else {
            LicenceIsOverActivity.start(this);
            finish();
        }
    }

    // TODO используя коллбеки жизненного цикла активити необходимо добавить возможность поставить игру на паузу при сворачивании окна
    // подсказка: механизм постановки на паузу можно подсмотреть в листенере кнопки PLAY/PAUSE

    // TODO необходимо добавить возможность выбора цвета летательного аппарата через ChoosePlayerModelActivity, используя этот код
    // player.setImageResource(ModelFragment.MODELS[position]);

    private void initClickListeners() {
        pauseClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseButton.setText("PLAY");
                playPauseButton.setOnClickListener(playClickListener);
                root.setOnClickListener(null);
                pauseAnimations();
            }
        };

        playClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame();
            }
        };

        playPauseButton.setOnClickListener(pauseClickListener);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChoosePlayerModelActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resumeGame() {
        playPauseButton.setText("PAUSE");
        playPauseButton.setOnClickListener(pauseClickListener);
        root.setOnClickListener(playerJumpClickListener);
        resumeAnimations();
    }

    private void resumeAnimations() {
        animatorManager.resume(dangerAnimator, KEY_DANGER_ANIMATOR);

        if (isJumpAnimatorWasActive) {
            isJumpAnimatorWasActive = false;
            returnToStartPosition();
        } else {
            animatorManager.resume(playerAnimator, KEY_PLAYER_ANIMATOR);
        }
    }

    private void pauseAnimations() {
        animatorManager.pause(dangerAnimator, KEY_DANGER_ANIMATOR);

        if (jumpAnimator != null) {
            isJumpAnimatorWasActive = true;
            animatorManager.pause(jumpAnimator, KEY_JUMP_ANIMATOR);
        } else {
            isJumpAnimatorWasActive = false;
        }

        animatorManager.pause(playerAnimator, KEY_PLAYER_ANIMATOR);
    }

    private void initGame() {
        root = findViewById(R.id.main_fl_root);
        player = (ImageView) findViewById(R.id.main_iv_player);
        danger = findViewById(R.id.main_v_danger);
        scoreTextView = (TextView) findViewById(R.id.main_tv_points);

        updateScoreTextView();
        collisionDetector = new CollisionDetector(this);

        playerJumpClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.setOnClickListener(null);
                startJumpAnimation();
            }
        };
        root.setOnClickListener(playerJumpClickListener);

        player.postDelayed(new Runnable() {
            @Override
            public void run() {
                startIntroAnimation();
            }
        }, 500);
    }

    private void updateScoreTextView() {
        String label = getString(R.string.label_score, currentGameScore);
        scoreTextView.setText(label);
    }

    private void startIntroAnimation() {
        DisplayMetrics dm = MainActivity.this.getResources().getDisplayMetrics();
        playerStartPosY = (dm.heightPixels / 2) - (player.getHeight() / 2);

        player.setTranslationX(-player.getWidth());
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(player, View.TRANSLATION_X, player.getWidth());
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(player, View.TRANSLATION_Y, playerStartPosY);
        ObjectAnimator dangerAnimator = ObjectAnimator.ofFloat(danger, View.TRANSLATION_Y, playerStartPosY);

        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(animatorY, animatorX, dangerAnimator);
        animator.setDuration(600);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                player.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startDangerAnimation();
                startPlaneAnimation();
            }
        });
        animator.start();
    }

    private void startDangerAnimation() {
        initDangerAnimator();
        dangerAnimator.start();
    }

    private void initDangerAnimator() {
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        dangerAnimator = ObjectAnimator.ofFloat(danger, View.TRANSLATION_X, displayWidth + 100, -200);
        dangerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        dangerAnimator.setRepeatMode(ValueAnimator.RESTART);
        dangerAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                danger.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                currentGameScore += 100;
                updateScoreTextView();
            }
        });
        dangerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        dangerAnimator.setDuration(1000 * 3);
    }

    private void startJumpAnimation() {
        disposeAnimator(playerAnimator);
        initJumpAnimator();
        jumpAnimator.start();
    }

    private void initJumpAnimator() {
        float translationY = player.getTranslationY();
        jumpAnimator = ObjectAnimator.ofFloat(player, View.TRANSLATION_Y,
                translationY, translationY - JUMP_VALUE);
        jumpAnimator.setRepeatCount(1);
        jumpAnimator.setRepeatMode(ValueAnimator.REVERSE);
        jumpAnimator.setDuration(800);
        jumpAnimator.addUpdateListener(collisionDetector);
        jumpAnimator.setInterpolator(new FastOutSlowInInterpolator());
        jumpAnimator.addListener(new AnimatorListenerAdapter() {

            private boolean isAnimationCanceled;

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimationCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isAnimationCanceled) {
                    jumpAnimator = null;
                    root.setOnClickListener(playerJumpClickListener);
                    returnToStartPosition();
                }
            }
        });
    }

    private void returnToStartPosition() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(player, View.TRANSLATION_Y, playerStartPosY - TRANSLATION_Y_DIFF);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startPlaneAnimation();
            }
        });
        animator.addUpdateListener(collisionDetector);
        animator.start();
    }

    private void startPlaneAnimation() {
        initPlayerAnimator();
        playerAnimator.start();
    }

    private void initPlayerAnimator() {
        float min = playerStartPosY - TRANSLATION_Y_DIFF;
        float max = playerStartPosY + TRANSLATION_Y_DIFF;

        playerAnimator = ObjectAnimator.ofFloat(player, View.TRANSLATION_Y, min, max);
        playerAnimator.setRepeatMode(ValueAnimator.REVERSE);
        playerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        playerAnimator.setDuration(ANIMATION_DURATION_PLANE);
        playerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        playerAnimator.addUpdateListener(collisionDetector);
    }

    private void disposeAnimator(ObjectAnimator animator) {
        if (animator != null) {
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
            animator.cancel();
        }
    }

    private void gameOver() {
        if (isFinishing()) {
            return;
        }

        root.setOnClickListener(null);

        disposeAnimator(jumpAnimator);
        disposeAnimator(playerAnimator);
        disposeAnimator(dangerAnimator);

        ResultsActivity.start(this, currentGameScore);
        finish();
    }

    static class CollisionDetector implements ValueAnimator.AnimatorUpdateListener {

        WeakReference<MainActivity> activityWeakReference;

        CollisionDetector(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final MainActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }

            float playerX1 = activity.player.getTranslationX();
            float playerY1 = activity.player.getTranslationY();
            float playerX2 = playerX1 + activity.player.getWidth();
            float playerY2 = playerY1 + activity.player.getHeight();

            float dangerX1 = activity.danger.getTranslationX();
            float dangerY1 = activity.danger.getTranslationY();
            float dangerX2 = dangerX1 + activity.danger.getWidth();
            float dangerY2 = dangerY1 + activity.danger.getHeight();

            if (playerX1 < dangerX2 && playerX2 > dangerX1 && playerY1 < dangerY2 && playerY2 > dangerY1) {
                Log.d("debug", "collision!");
                activity.gameOver();
                animation.removeAllUpdateListeners();
            }
        }

    }

}
