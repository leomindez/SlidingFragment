package com.bldev.modalviewfragment;


import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by iclicmx on 09/01/14.
 */
public class ModalViewFragment extends Fragment {

    private static ModalViewFragment modalViewFragment = null;
    private OnModalViewFragmentAnimationEnd onModalViewFragmentAnimationEnd;
    private View.OnClickListener onClickListener;



 public static ModalViewFragment newInstance(){

     if(modalViewFragment== null){
         modalViewFragment = new ModalViewFragment();
         return modalViewFragment;
     }else{
         return modalViewFragment;
     }

 }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainCointainer = inflater.inflate(R.layout.fragment_main,container,false);
        mainCointainer.setOnClickListener(onClickListener);
        return mainCointainer;
    }





    public void setOnClickListener(View.OnClickListener listener){
        this.onClickListener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof  OnModalViewFragmentAnimationEnd)){
            throw new ClassCastException("Activity must implement OnModalViewFragmentAnimationEnd interface");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return getAnimation(enter,onModalViewFragmentAnimationEnd);
    }


    
    private Animation getAnimation(boolean enter,final OnModalViewFragmentAnimationEnd  onModalViewFragmentAnimationEnd){
        int id = enter?R.anim.slide_fragment_in_support:R.anim.slide_fragment_out_support;

        final Animation animation = AnimationUtils.loadAnimation(getActivity(),id);
        if (enter){

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onModalViewFragmentAnimationEnd.onAnimationEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        return animation;
    }

    public void setOnModalViewFragmentAnimationEnd(OnModalViewFragmentAnimationEnd onModalView){
        this.onModalViewFragmentAnimationEnd = onModalView;
    }



}
