package com.clakestudio.pc.startapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.volokh.danylo.hashtaghelper.HashTagHelper;


public class MainActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private SharedPreferences sharedPreferences;
    private Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("profile", false).apply();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


    }

    @Override
    protected void onStart() {

        super.onStart();
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

        editTextSearch.setVisibility(View.GONE);
        final ImageButton imageButton = (ImageButton) findViewById(R.id.buttonSearch);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibility(editTextSearch);
            }
        });


        HashTagHelper hashTagHelper = HashTagHelper.Creator.create(Color.parseColor("#E57373"), null);
        hashTagHelper.handle(editTextSearch);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {

                    if (editTextSearch.getText().toString().isEmpty() || !editTextSearch.getText().toString().contains("#") || editTextSearch.getText().toString().contains(" ")) {

                        if (editTextSearch.getText().toString().contains(" ") || editTextSearch.getText().toString().contains(",")) {
                            Toast.makeText(getApplicationContext(), R.string.wed_advise_to_search_one_particular, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.search_is_empty_or_does_not_contain, Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                        intent.putExtra("hashTag", editTextSearch.getText().toString());
                        startActivity(intent);
                    }

                    return true;
                }
                if (i == KeyEvent.KEYCODE_DEL && editTextSearch.getText().toString().isEmpty()) {
                    editTextSearch.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    return true;
                }

                return false;
            }
        });


    }

    private void changeVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (firebaseUser == null) {
            getMenuInflater().inflate(R.menu.main_anonymous, menu);
        } else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_settings: {

                if (firebaseUser == null) {
                    if (sharedPreferences.getBoolean("anonymous", false)) {
                        Toast.makeText(getApplicationContext(), R.string.anonymous_users_can_not_view_account, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.log_in_to_view_account, Toast.LENGTH_LONG).show();
                        nextActivity = new Intent(getApplicationContext(), StartActivity.class);
                    }
                } else {
                    nextActivity = new Intent(getApplicationContext(), ProfileActivity.class);
                }
                break;
            }
            case R.id.add_app: {
                if (firebaseUser == null) {
                    Toast.makeText(getApplicationContext(), R.string.create_an_account_log_in_firts, Toast.LENGTH_LONG).show();

                } else {
                    getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("edit", false).apply();
                    nextActivity = new Intent(getApplicationContext(), CategorySelectActivity.class);

                }
                break;
            }

            case R.id.start_activity: {
                if (firebaseUser != null) {
                    firebaseAuth.signOut();
                    sharedPreferences.edit().putBoolean("anonymous", false).apply();
                    Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();
                    nextActivity = new Intent(getApplicationContext(), StartActivity.class);
                } else {
                    sharedPreferences.edit().putBoolean("anonymous", false).apply();
                    nextActivity = new Intent(getApplicationContext(), StartActivity.class);
                }
                break;
            }
            case R.id.privacy_policy: {
                nextActivity = new Intent(Intent.ACTION_VIEW, Uri.parse("https://privacypolicyclake.blogspot.com/2017/09/startapp-privacy-policy.html"));
                break;

            }
            case R.id.app_info: {
                nextActivity = new Intent(getApplicationContext(), InfoActivity.class);
                break;
            }
        }
        startActivity(nextActivity);
        finish();
        return super.onOptionsItemSelected(item);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[]{
                "Hot", "New", "Categories"
        };
        Context context;

        PagerAdapter(android.support.v4.app.FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            this.context = context;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new BlankFragment();
                case 1:
                    return new TimeFragment();
                case 2:
                    return new CategoryFragment();
            }
            return null;


        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        View getTabView(int position) {
            View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView textView = (TextView) tab.findViewById(R.id.custom_text);
            textView.setText(tabTitles[position]);
            return tab;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
