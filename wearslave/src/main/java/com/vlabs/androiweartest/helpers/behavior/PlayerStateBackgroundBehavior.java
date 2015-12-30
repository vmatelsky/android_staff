package com.vlabs.androiweartest.helpers.behavior;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.vlabs.androiweartest.models.PlayerStateBackgroundModel;
import com.vlabs.wearmanagers.Receiver;

public class PlayerStateBackgroundBehavior {

    private final ImageView mImageView;
    private final PlayerStateBackgroundModel mBackgroundModel;
    private final Receiver<Drawable> mOnBackgroundChanged = new Receiver<Drawable>() {
        @Override
        public void receive(final Drawable drawable) {
            mImageView.setImageDrawable(drawable);
        }
    };

    public PlayerStateBackgroundBehavior(final ImageView imageView, final PlayerStateBackgroundModel backgroundModel) {
        mImageView = imageView;
        mBackgroundModel = backgroundModel;
        mBackgroundModel.addOnBackgroundChangedListener(mOnBackgroundChanged);
    }

}
