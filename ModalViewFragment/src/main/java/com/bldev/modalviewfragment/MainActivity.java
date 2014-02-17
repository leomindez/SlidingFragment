package com.bldev.modalviewfragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener,OnModalViewFragmentAnimationEnd{


    private Button showModalView;
    private ModalViewFragment modalViewFragment;
    boolean mDidSlideOut = false;
    boolean mIsAnimating = false;
    private FrameLayout mainContainer;
    private MainFragment mainFragment;
    private View mDarkHoverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        modalViewFragment = new ModalViewFragment();
        mainContainer = (FrameLayout)findViewById(R.id.container);
        mainFragment = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        mDarkHoverView = findViewById(R.id.dark_hover_view);
        ViewHelper.setAlpha(mDarkHoverView,0);




        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment();
            }
        };
        modalViewFragment.setOnClickListener(listener);
        mainFragment.setOnClickListener(listener);
        mDarkHoverView.setOnClickListener(listener);
        modalViewFragment.setOnModalViewFragmentAnimationEnd(this);

    }


    private void switchFragment(){
        if (mIsAnimating) {
            return;
        }
        mIsAnimating = true;
        if (mDidSlideOut) {
            mDidSlideOut = false;
            getSupportFragmentManager().popBackStack();
        }else{
            mDidSlideOut = true;
            Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_fragment_in_support, 0, 0, R.anim.slide_fragment_out_support);
                    fragmentTransaction.add(R.id.container,modalViewFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            };

            slideBack(animatorListener);
        }



    }

    private void slideBack(Animator.AnimatorListener animatorListener) {

        View movingFragmentView = mainFragment.getView();


        PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotationX",40f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",0.8f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",0.8f);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(movingFragmentView,rotateX,scaleX,scaleY);
        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(movingFragmentView,"rotationX",0);
        ObjectAnimator darkHoverViewAnimator = ObjectAnimator.
                ofFloat(mDarkHoverView, "alpha", 0.0f, 0.5f);

        movingFragmentRotator.setStartDelay(getResources().getInteger(R.integer.half_slide_up_down_duration));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator,darkHoverViewAnimator,movingFragmentRotator);
        animatorSet.addListener(animatorListener);
        animatorSet.start();
    }



    @Override
    public void onBackStackChanged() {
        if (!mDidSlideOut){
            slideForward(null);
        }
    }

    private void slideForward(Animator.AnimatorListener animatorListener) {

        View movingFragmentView = mainFragment.getView();


        PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotationX",40f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",1.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",1.0f);

        ObjectAnimator movingFragmentAnimator  = ObjectAnimator.ofPropertyValuesHolder(movingFragmentView,rotateX,scaleX,scaleY);
        ObjectAnimator darkHoverViewAnimator = ObjectAnimator.
                ofFloat(mDarkHoverView, "alpha", 0.5f, 0.0f);
        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(movingFragmentView,"rotationX",0);
        movingFragmentRotator.setStartDelay( getResources().getInteger(R.integer.half_slide_up_down_duration));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(movingFragmentAnimator,movingFragmentRotator,darkHoverViewAnimator);
        animatorSet.setStartDelay(getResources().getInteger(R.integer.slide_up_down_duration));
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;
            }
        });
        animatorSet.start();
    }

    @Override
    public void onAnimationEnd() {
        mIsAnimating = false;
    }
}
