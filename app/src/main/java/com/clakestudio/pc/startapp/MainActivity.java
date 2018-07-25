package com.clakestudio.pc.startapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

    Toolbar toolbar;
    EditText editTextSearch;
    HashTagHelper hashTagHelper;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("profile", false).apply();

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i<tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);









    }

    @Override
    protected void onStart() {

        super.onStart();
        editTextSearch = (EditText)findViewById(R.id.editTextSearch);

        editTextSearch.setVisibility(View.GONE);
        final ImageButton imageButton = (ImageButton)findViewById(R.id.buttonSearch);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextSearch.getVisibility()==View.GONE) {
                    editTextSearch.setVisibility(View.VISIBLE);

                }
                else {
                    editTextSearch.setVisibility(View.GONE);

                }



            }
        });


        hashTagHelper = HashTagHelper.Creator.create(Color.parseColor("#E57373"), null);
        hashTagHelper.handle(editTextSearch);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i==EditorInfo.IME_ACTION_DONE) {

                    if (editTextSearch.getText().toString().isEmpty() || !editTextSearch.getText().toString().contains("#") || editTextSearch.getText().toString().contains(" ")){

                        if (editTextSearch.getText().toString().contains(" ") || editTextSearch.getText().toString().contains(",")) {
                            Toast.makeText(getApplicationContext(), "We advise to search one particular functions/ delete space", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Search is empty/does not contain #hashtag", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                        intent.putExtra("hashTag", editTextSearch.getText().toString());
                        startActivity(intent);
                    }

                    return true;
                }
                if (i==keyEvent.KEYCODE_DEL && editTextSearch.getText().toString().isEmpty()) {
                    editTextSearch.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    return true;
                }

                return false;
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (firebaseUser==null) {
            getMenuInflater().inflate(R.menu.main_anonymous, menu);
        }
        else {
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
                            Toast.makeText(getApplicationContext(), "Anonymous users can not view account", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Log in to view account", Toast.LENGTH_LONG).show();
                            Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                            startActivity(startIntent);
                            finish();
                        }
                    } else {
                        Intent addApp = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(addApp);
                        finish();

                    }
                    return true;

                }


                case R.id.add_app: {

                    // check wheter user logged or no

                    if (firebaseUser == null) {
                            Toast.makeText(getApplicationContext(), "Create an account/log in first", Toast.LENGTH_LONG).show();

                    } else {
                        Intent addApp = new Intent(getApplicationContext(), CategorySelectActivity.class);
                     getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("edit", false).apply();
                        startActivity(addApp);
                        finish();

                    }
                    return true;
                }

                case R.id.start_activity: {

                    if (firebaseUser != null) {
                        firebaseAuth.signOut();
                        sharedPreferences.edit().putBoolean("anonymous", false).apply();
                        Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();
                        Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(startIntent);
                        finish();
                    } else {
                        sharedPreferences.edit().putBoolean("anonymous", false).apply();
                        Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(startIntent);
                        finish();
                    }

                    return true;
                }
                case R.id.privacy_policy: {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://privacypolicyclake.blogspot.com/2017/09/startapp-privacy-policy.html"));
                    startActivity(browserIntent);


                    return true;
                }
                case R.id.app_info: {
                    Intent startIntent = new Intent(getApplicationContext(), InfoActivity.class);
                    startActivity(startIntent);
                    finish();
                }


            }

        return  super.onOptionsItemSelected(item);
    }

    class PagerAdapter extends FragmentPagerAdapter{

        String tabTitles[] = new String[] {
         "Hot", "New", "Categories"
        };
        Context context;

        public PagerAdapter(android.support.v4.app.FragmentManager fragmentManager,Context context ){
            super(fragmentManager);
            this.context=context;
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
        public View getTabView(int position) {
            View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView textView = (TextView)tab.findViewById(R.id.custom_text);
            textView.setText(tabTitles[position]);
            return tab;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
