package com.hi.andy.supervocsdk28;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener{

        private static Boolean isExit = false;
        private static Boolean hasTask = false;


        String Thline = "";
        String appVersion;
        int vocs = 0;
        String E;
    String C;
    String h;

        Timer timerExit = new Timer();
        TimerTask task = new TimerTask() {
     @Override
     public void run() {
        isExit = false;
        hasTask = true;

     }
};
    static final String db_name = "VOC";
        static final String tb_name = "VOC";
        SQLiteDatabase db;
        Cursor c;
        private BottomNavigationView navigation;
        private ViewPager viewPager;
        private editFragment fragment1 = new editFragment();
        private allFragment fragment2 = new allFragment();
        private otherFragment fragment3 = new otherFragment();
        private settingsFragment fragment4 = new settingsFragment();

@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.default_value:
                EditText c_three = (EditText) findViewById(R.id.tline_name);
                c_three.setText(R.string.third_preset_text);
                Thline = getString(R.string.third_preset_text);
                SharedPreferences pref = getSharedPreferences("third", MODE_PRIVATE);
                Toasty.success(this, getString(R.string.back_to_default), Toast.LENGTH_SHORT, true).show();
                pref.edit()
                        .putString("third", Thline)
                        .apply();
                return true;
            case R.id.about:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.about_text) +"\nv" + appVersion)
                        .setTitle(R.string.about_app)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
                return true;
            case R.id.check_C:
                TextView allC = (TextView) findViewById(R.id.allC);
                item.setChecked(!item.isChecked());
                if(item.isChecked()){
                    allC.setVisibility(TextView.VISIBLE);
                }
                else {
                    allC.setVisibility(TextView.INVISIBLE);
                }

                return true;
            case R.id.check_E:
                TextView allE = (TextView) findViewById(R.id.allE);
                item.setChecked(!item.isChecked());
                if(item.isChecked()){
                    allE.setVisibility(TextView.VISIBLE);
                }
                else {
                    allE.setVisibility(TextView.INVISIBLE);
                }
                return true;
            case R.id.check_h:
                TextView allh = (TextView) findViewById(R.id.allh);
                item.setChecked(!item.isChecked());
                if(item.isChecked()){
                    allh.setVisibility(TextView.VISIBLE);
                }
                else {
                    allh.setVisibility(TextView.INVISIBLE);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Thline = getSharedPreferences("third", MODE_PRIVATE)
                    .getString("third",getString(R.string.third_preset_text));
            //ver
            PackageManager manager = this.getPackageManager();
            try {
                PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                appVersion = info.versionName; //版本名
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //ver
            //db
            db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
            String createTable = "CREATE TABLE IF NOT EXISTS " + tb_name + "(eng VARCHAR(64)," + "chn VARCHAR(32)," + "hint VARCHAR(16))";
            db.execSQL(createTable);
            requary();
            //db
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPager.addOnPageChangeListener(this);
            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            return fragment1;
                        case 1:
                            return fragment2;
                        case 2:
                            return fragment3;
                        case 3:
                            return fragment4;
                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 4;
                }
            });
            //
        }



    public void onPageSelected(int position) {
        //页面滑动的时候，改变BottomNavigationView的Item高亮
        navigation.getMenu().getItem(position).setChecked(true);
        change_frag(position);

    }
        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //点击BottomNavigationView的Item项，切换ViewPager页面
                //change_frag(item.getOrder());
                viewPager.setCurrentItem(item.getOrder());
                return true;
            }
        };




private void change_frag(int num){
    TextView allE = (TextView) findViewById(R.id.allE);
    TextView allC = (TextView) findViewById(R.id.allC);
    TextView allh = (TextView) findViewById(R.id.allh);
    switch (num) {
        case 0://edit
break;
        case 1://all
           if(vocs == 0) {
                allC.setTextSize(12);
                allC.setText(getString(R.string.there_isn_t_any_voc));
                allE.setText("");
                allh.setText("");
            } else{
            allC.setTextSize(24);
            allC.setText(C);
            allE.setText(E);
            allh.setText(h);
        }
            break;
        case 2://other
            TextView voc_num = (TextView) findViewById(R.id.voc);
            voc_num.setText("number of voc:" + String.valueOf(vocs));
            break;
        case 3://settings
            EditText default3 =(EditText)findViewById(R.id.tline_name);
            default3.setText(Thline);
            break;
    }
}


        @Override
        public void onPageScrollStateChanged(int state) {

        }

    int noer = 0;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onPrepareOptionsMenu(Menu menu){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        switch (viewPager.getCurrentItem()){
            case 0://edit
                menu.findItem(R.id.default_value).setVisible(false);
                menu.findItem(R.id.check_C).setVisible(false);
                menu.findItem(R.id.check_E).setVisible(false);
                menu.findItem(R.id.check_h).setVisible(false);
                break;
            case 1://all

                TextView allE = (TextView) findViewById(R.id.allE);
                menu.findItem(R.id.check_h).setTitle(getString(R.string.show_label, Thline));
                menu.findItem(R.id.default_value).setVisible(false);
                if(allE.getText() != ""){
                    menu.findItem(R.id.check_C).setVisible(true);
                    menu.findItem(R.id.check_E).setVisible(true);
                    menu.findItem(R.id.check_h).setVisible(true);
                }
                break;
            case 2://other
                menu.findItem(R.id.default_value).setVisible(false);
                menu.findItem(R.id.check_C).setVisible(false);
                menu.findItem(R.id.check_E).setVisible(false);
                menu.findItem(R.id.check_h).setVisible(false);
                break;
            case 3://settings
                menu.findItem(R.id.default_value).setVisible(true);
                menu.findItem(R.id.check_C).setVisible(false);
                menu.findItem(R.id.check_E).setVisible(false);
                menu.findItem(R.id.check_h).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @SuppressLint("WrongConstant")
    public void add(View V) {

            TextInputLayout Ie_v = (TextInputLayout) findViewById(R.id.IE_v);
            TextInputLayout Ic_v = (TextInputLayout) findViewById(R.id.IC_v);

            EditText IEnglish = (EditText) findViewById(R.id.IEnglish);
            EditText IChinese = (EditText) findViewById(R.id.IChinese);
            EditText Ihint = (EditText) findViewById(R.id.Ihint);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            if(IEnglish.length() == 0 && IChinese.length() == 0) {
                Toasty.error(this, getString(R.string.kidding), Toast.LENGTH_SHORT, true).show();
                Ic_v.setError("");
                Ie_v.setError("");
                noer += 1;
            }
            else if (IEnglish.length() == 0) {
                Ie_v.setError(getResources().getString(R.string.no_input));
                Ic_v.setError("");
                noer =+ 1;
            }
            else if(IChinese.length() == 0) {
                Ic_v.setError(getResources().getString(R.string.no_input));
                Ie_v.setError("");
                noer =+ 1;
            }

            else {
                Toast warm = Toasty.warning(this, getString(R.string.word_10), Toast.LENGTH_SHORT, true);
                warm.setGravity(Gravity.CENTER, 0, 0);
                if (Ihint.length() == 0) {
                    addData(IEnglish.getText().toString(), IChinese.getText().toString(), "-");
                } else {
                    addData(IEnglish.getText().toString(), IChinese.getText().toString(), Ihint.getText().toString());//toString不知是否要加//update:because it not imput String is bolleon
                }
                Snackbar.make(findViewById(R.id.viewPager),R.string.added, Snackbar.LENGTH_SHORT).show();
                if(IEnglish.length() > 10 || IChinese.length() > 10 || Ihint.length() > 10){
                    warm.show();
                }
                requary();
                noer = 0;
            }
            if (noer == 0) {
                Ie_v.setError("");
                Ic_v.setError("");
                IEnglish.setText("");
                IChinese.setText("");
                IEnglish.clearFocus();
                IChinese.clearFocus();
                Ihint.clearFocus();
                imm.hideSoftInputFromWindow(IEnglish.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(IChinese.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(Ihint.getWindowToken(), 0);
            }
        }

        private void addData(String ENG, String CHN, String hint) {

                ContentValues cv = new ContentValues(3);
                cv.put("eng", ENG);
                cv.put("chn", CHN);
                cv.put("hint", hint);

                db.insert(tb_name, null, cv);

                db.close();
                db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        }
        public void del(View V) {
            EditText delete_input = (EditText) findViewById(R.id.delete_input);
        if(delete_input.length() == 0){
                Toast.makeText(this, R.string.no_use, Toast.LENGTH_SHORT).show();
            }
            else{
                new AlertDialog.Builder(this)
                        .setMessage("del '" + delete_input.getText().toString() + "' ?")
                        .setTitle(R.string.really)
                        .setPositiveButton(R.string.sure, this)
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            EditText delete_input = (EditText) findViewById(R.id.delete_input);
            db.delete(tb_name, "eng" + "=?", new String[]{String.valueOf(delete_input.getText().toString())});
            delete_input.setText("");
            requary();
            //del
            }

            private void requary() {
                c = db.rawQuery("SELECT * FROM " + tb_name, null);
                vocs = 0;
                if (c.moveToFirst()) {
                    //textsize 24
                    vocs += 1;
                    E = getString(R.string.English)+"\n\n";
                    C = getString(R.string.Chinese) +"\n\n";
                    h = Thline +"\n\n";
                    do {
                        E += c.getString(0) + "\n";
                        C += c.getString(1) + "\n";
                        h += c.getString(2) + "\n";

                    } while (c.moveToNext());}


    }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
        public boolean onKeyDown(int keyCode, KeyEvent event) {


            // 判斷是否按下Back
      if (keyCode == KeyEvent.KEYCODE_BACK) {
                // 是否要退出
         if(!isExit) {

               isExit = true; //記錄下一次要退出

               Toast.makeText(this, R.string.press_back_to_leave

                            , Toast.LENGTH_SHORT).show();

                    // 如果超過兩秒則恢復預設值
               if(!hasTask) {

                    timerExit.schedule(task, 2000);
               }

         } else {

              finish(); // 離開程式
              System.exit(0);

         }
      }
            return false;
 }

 public void change_three(View v){
        EditText c_three = (EditText) findViewById(R.id.tline_name);
        SharedPreferences pref = getSharedPreferences("third", MODE_PRIVATE);
    if(c_three.length() == 0) {
        Toasty.error(this, getString(R.string.no_input), Toast.LENGTH_SHORT, true).show();
        }
        else {
        Thline = c_three.getText().toString();
        pref.edit()
                .putString("third", Thline)
                .apply();
        c_three.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(c_three.getWindowToken(), 0);
        pref.edit()
                .putString("third", Thline)
                .apply();
        Toasty.success(this, getString(R.string.success, Thline), Toast.LENGTH_SHORT, true).show();
    }
 }


}
/*
尚未做的事：
* 編輯
*1 優化(ListView)
*2 刪除
*3 如果已經加過了,不給加(int,String.indexOf(""),感謝 JJ Wang)
*
* 全部
*1 換成ListView
*2 可選排序方法
*
* 更多
*1 function(抽/念...)
*2 think**
*
* 設定
*1 選擇如果沒打註解,取代的文字
*2 字體大小？
*
*  other
* 禿然忘記了 ('~')
*/
