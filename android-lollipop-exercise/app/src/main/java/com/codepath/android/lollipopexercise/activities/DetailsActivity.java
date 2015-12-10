package com.codepath.android.lollipopexercise.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.lollipopexercise.R;
import com.codepath.android.lollipopexercise.models.Contact;
import com.codepath.android.lollipopexercise.utils.ImageUtils;
import com.codepath.android.lollipopexercise.utils.ViewUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    private Contact mContact;
    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvPhone;
    private View vPalette;
    private FloatingActionButton fab;

    private Transition.TransitionListener mEnterTransitionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        vPalette = findViewById(R.id.vPalette);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setVisibility(View.INVISIBLE);

        // Extract contact from bundle
        mContact = (Contact)getIntent().getExtras().getSerializable(EXTRA_CONTACT);
        if (mContact == null) {
            finish();
            return;
        }

        // Fill views with data
        tvName.setText(mContact.getName());
        tvPhone.setText(mContact.getNumber());

        Target target = ImageUtils.getTargetForVibrantColorInto(ivProfile, vPalette);
        ivProfile.setTag(target);
        Picasso.with(this).load(mContact.getThumbnailDrawable()).into(target);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + mContact.getNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        mEnterTransitionListener = new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                ViewUtils.enterReveal(DetailsActivity.this, fab, mEnterTransitionListener);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        };
        getWindow().getEnterTransition().addListener(mEnterTransitionListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ViewUtils.exitReveal(DetailsActivity.this, fab);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
