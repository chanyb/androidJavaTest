package kr.co.kworks.smartmedicinebox.networks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;

import kr.co.kworks.smartmedicinebox.GlobalApplication;
import kr.co.kworks.smartmedicinebox.R;
import kr.co.kworks.smartmedicinebox.ui.intro.IntroActivity;
import kr.co.kworks.smartmedicinebox.ui.login.LoginActivity;
import kr.co.kworks.smartmedicinebox.ui.main.MainActivity;
import kr.co.kworks.smartmedicinebox.ui.main.NativeMainActivity;
import kr.co.kworks.smartmedicinebox.utils.CustomConfirmDialog;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private CustomConfirmDialog mCustomConfirmDialog;

    @Override
    public void onReceive(Context mContext, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()==null) {
            // network disconnected
            Log.i("this", "네트웤 연결 해제");

            if(GlobalApplication.getCurrentActivity() instanceof NativeMainActivity) {
                return; // 이미 NativeMainActivity인 경우, return;
            }

            Intent i = new Intent(GlobalApplication.getCurrentActivity(), NativeMainActivity.class);
            if(MainActivity.mContext != null) {
                Intent dataIntent = MainActivity.mContext.getIntent();
                i.putExtra("selectType", dataIntent.getStringExtra("selectType"));
            }

            if (mCustomConfirmDialog != null) if (mCustomConfirmDialog.isShowing()) return;
            mCustomConfirmDialog = new CustomConfirmDialog(GlobalApplication.getCurrentActivity(), GlobalApplication.getCurrentActivity().getString(R.string.network_closed), GlobalApplication.getCurrentActivity().getString(R.string.confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalApplication.getCurrentActivity().startActivity(i);
                    GlobalApplication.getCurrentActivity().finish();
                    GlobalApplication.getCurrentActivity().overridePendingTransition(0,0);
                    mCustomConfirmDialog.dismiss();
                }
            });
            mCustomConfirmDialog.setCancelable(false);

            try {
                if (MainActivity.mContext != null) mCustomConfirmDialog.show();
            } catch (Exception e) {
                Log.i("this", "error: ", e);
            }

            return;
        } else if (connectivityManager.getActiveNetworkInfo()!= null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            Log.i("this", "네트웤 연결");
            try {
                if (mCustomConfirmDialog != null) if (mCustomConfirmDialog.isShowing()) mCustomConfirmDialog.dismiss();
            } catch (Exception e) {
                Log.i("this", "error: ", e);
            }
            if(GlobalApplication.getCurrentActivity() instanceof NativeMainActivity) {
                Intent i = new Intent(GlobalApplication.getCurrentActivity(), IntroActivity.class);
                GlobalApplication.getCurrentActivity().startActivity(i);
                GlobalApplication.getCurrentActivity().finish();
            }

        }
    }
}
