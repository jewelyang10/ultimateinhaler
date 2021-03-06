package monash.ultimateinhaler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.joshdholtz.sentry.Sentry;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

import monash.ultimateinhaler.service.NetworkService;
import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class StartActivity extends AppCompatActivity implements NetworkService.ConnectivityReceiverListener  {
    private TextView mTextView;
    private int previousPosition;
    private Typeface ty1;
    private static final int REQUEST_CODE = 1234;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // Sentry will look for uncaught exceptions from previous runs and send them
        Sentry.init(this.getApplicationContext(), "https://4c9327e0877c45bcbacee3fff7a38236:193fac1f37f74ba29983ecb1fb10be3f@sentry.io/96599");
        Sentry.captureMessage("Test test test");
//        this.startService(new Intent(this, autoRecordService.class));

        loadTutorial();

        //Configure the typeface
        ty1 = Typeface.createFromAsset(getAssets(), "fonts/OswBold.ttf");

        //Customize the title bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTextView.setText("Ultimate Inhaler");
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTypeface(ty1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //  getSupportActionBar().setIcon(R.drawable.applogoblack);
        //noinspection deprecation
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbarbg));

//        if (!CheckNetwork()) {
//
//            Toast.makeText(StartActivity.this, "No internet!Please check your internet!", Toast.LENGTH_SHORT).show();
//            openDialog();
//        }

        checkConnection();

        //Set the fragment initially
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
        fragmentTransaction.commit();


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                String title = "";
                switch (tabId) {
                    case R.id.nav_home:
                        title = "Ultimate Inhaler";
                        MainFragment fragment = new MainFragment();
                        FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_containerStart, fragment);
                        fragmentTransaction.addToBackStack("Fragment");
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_hospital:
                        title = "NEARBY";

                        FindHospitalsFragment fragment2 = new FindHospitalsFragment();
                        FragmentTransaction fragmentTransaction2 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.fragment_containerStart, fragment2);
                        fragmentTransaction2.addToBackStack("FragmentB");
                        fragmentTransaction2.commit();
                        break;
                    case R.id.nav_tips:
                        title = "TIPS";

//                        TipsFragment fragment3 = new TipsFragment();
                        TabsNewsTipsFragment fragment3 = new TabsNewsTipsFragment();
                        FragmentTransaction fragmentTransaction3 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.fragment_containerStart, fragment3);
                        fragmentTransaction3.addToBackStack("FragmentB");
                        fragmentTransaction3.commit();

                        break;
                    case R.id.nav_diary:
                        title = "DIARY";

                        CalendarFragment fragment4 = new CalendarFragment();
                        FragmentTransaction fragmentTransaction4 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction4.replace(R.id.fragment_containerStart, fragment4);
                        fragmentTransaction4.addToBackStack("FragmentB");
                        fragmentTransaction4.commit();
                        break;

                    case R.id.nav_more:
                        title = "MORE";

                        MoreFragment fragment6 = new MoreFragment();
                        FragmentTransaction fragmentTransaction6 =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction6.replace(R.id.fragment_containerStart, fragment6);
                        fragmentTransaction6.addToBackStack("FragmentB");
                        fragmentTransaction6.commit();
                        break;
                }
                // update selected fragment and title
                if (getSupportActionBar() != null) {
                    mTextView.setText(title);
                    mTextView.setTypeface(ty1);

                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
            }
        });


    }

    private void checkConnection() {
        boolean isConnected = NetworkService.isConnected();
        showSnack(isConnected);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Your device is not connected to internet. Please check the connection!";
//            new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("No connection")
//                    .setContentText(message)
//                    .setConfirmText("OK!")
//                    .show();
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.bottomBar), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();

        }


    }
    @Override
    public void onBackPressed() {
    }


    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.slide_1_biodiversity, R.string.slide_1_biodiversity_subtitle,
                R.color.slide_1, R.drawable.transparent, R.drawable.tut_page_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.slide_2_diary, R.string.slide_2_diary_subtitle,
                R.color.slide_2, R.drawable.tut_page_2_front, R.drawable.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem(R.string.slide_3_tips, R.string.slide_3_tips_subtitle,
                R.color.slide_3, R.drawable.tut_page_3_foreground, R.drawable.tut_page_3_background);

        TutorialItem tutorialItem4 = new TutorialItem(R.string.slide_4_weather, R.string.slide_4_weather_subtitle,
                R.color.slide_4, R.drawable.tut_page_3_background, R.drawable.tut_page_4_background);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            Toast.makeText(this, "Tutorial finished", Toast.LENGTH_LONG).show();

        }
    }

    public void setToolBar(String title, String subtitle) {


        if (subtitle != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
            TextView subTextView = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
            mTextView.setText(title);
            mTextView.setTextColor(Color.WHITE);
            subTextView.setTextColor(Color.WHITE);
            subTextView.setText(subtitle);
            subTextView.setVisibility(View.VISIBLE);

        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
            TextView subTextView = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
            mTextView.setText(title);
            subTextView.setVisibility(View.GONE);
            mTextView.setTextColor(Color.WHITE);
            subTextView.setTextColor(Color.WHITE);
        }

    }

    public void setDiaryToolBarColor() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView subTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        subTextView.setTextColor(Color.WHITE);
    }

    public void setToolBarColor() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setTextColor(Color.RED);
    }
}
