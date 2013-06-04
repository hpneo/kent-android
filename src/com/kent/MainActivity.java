package com.kent;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends SherlockActivity {
  private ActionBar actionBar = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_PROGRESS);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    
    super.onCreate(savedInstanceState);
    
    SharedPreferences preferences = getSharedPreferences("KentPreferences", MODE_PRIVATE);
    
    if (preferences.getBoolean("signed_in", false)) {
      setContentView(R.layout.activity_main);
      
      this.actionBar = this.getSupportActionBar();
      this.actionBar.setTitle("KENT");
      this.actionBar.setHomeButtonEnabled(true);
      // this.actionBar.setDisplayHomeAsUpEnabled(true); // Only for children activities 
      this.actionBar.setDisplayShowHomeEnabled(true);
      
      this.setSupportProgressBarIndeterminateVisibility(false);
    }
    else {
      Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      this.startActivity(intent);
      this.finish();
    }
    
    SharedPreferences.Editor preferencesEditor = preferences.edit();
    
    preferencesEditor.clear();
    preferencesEditor.commit();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem refreshAction = menu.add("Refresh");
    refreshAction.setIcon(R.drawable.refresh);
    refreshAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    refreshAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        setSupportProgressBarIndeterminateVisibility(true);
        item.setVisible(false);
        return false;
      }
    });
    
//    SubMenu subMenu1 = menu.addSubMenu("Action Item");
//    subMenu1.add("Sample");
//    subMenu1.add("Menu");
//    subMenu1.add("Items");
//
//    MenuItem subMenu1Item = subMenu1.getItem();
//    subMenu1Item.setIcon(R.drawable.refresh);
//    subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    return super.onCreateOptionsMenu(menu);
  }
}
