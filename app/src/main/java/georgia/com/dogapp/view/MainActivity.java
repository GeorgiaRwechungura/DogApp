package georgia.com.dogapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import georgia.com.dogapp.R;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    private static  final int PERMISSION_SEND_SMS=32;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController= Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this,navController);
        fragment= getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(navController,(DrawerLayout) null);
    }

   public  void checkSmsPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){
                new AlertDialog.Builder(this)
                        .setTitle("Send Sms Permission")
                        .setMessage("This app require permission to send sms")
                        .setPositiveButton("Ask me", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSmsPermission();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notifyDetailFragment(false);
                            }
                        })
                        .show();


            }else{
                requestSmsPermission();
            }

        }else{
            notifyDetailFragment(true);
        }

    }

    private void requestSmsPermission() {
        String [] permissions={Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this,permissions, PERMISSION_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_SEND_SMS:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    notifyDetailFragment(true);
                }else {
                    notifyDetailFragment(false);
                }

                break;
            }

        }
    }

    private void notifyDetailFragment(Boolean permissionGranted) {
        Fragment activeFragment=fragment.getChildFragmentManager().getPrimaryNavigationFragment();
        if(activeFragment instanceof DetailFragment){
            ( (DetailFragment) activeFragment).onPermissionResult(permissionGranted);
        }
    }
}
