package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PhotoGridFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoGridActivity extends AppCompatActivity {
    @Bind(R.id.photoGridFL) FrameLayout photoGridFL;

    PhotoGridFragment photoGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Create the transaction
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        // Replace the content of the container
        photoGridFragment = PhotoGridFragment.newInstance(
            extras.getString("type"),
            extras.getString("tagName"),
            extras.getString("userId")
        );
        fts.replace(R.id.photoGridFL, photoGridFragment);
        // Commit the changes
        fts.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is NOT part of this app's task, so create a new task
//                    // when navigating up, with a synthesized back stack.
//                    TaskStackBuilder.create(this)
//                            // Add all of this activity's parents to the back stack
//                            .addNextIntentWithParentStack(upIntent)
//                                    // Navigate up to the closest parent
//                            .startActivities();
//                } else {
//                    // This activity is part of this app's task, so simply
//                    // navigate up to the logical parent activity.
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
