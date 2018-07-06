package com.tony.tonythetiger.team_plane.Kakao;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

/**
 * Created by tony on 2017-11-28.
 */

public class SessionCallback implements ISessionCallback {
    private Activity activity;

    public SessionCallback(Activity currentActivity) {
        this.activity = currentActivity;
    }

    @Override
    public void onSessionOpened() {

        Log.d("debug", "onSessionOpened()");

        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.d("debug", "session error - "+message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    activity.finish();
                } else {
                    //redirectMainActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                Log.d("debug", "session success");
                activity.finish();
            }
        });

    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        // 세션 연결이 실패했을때
        // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
    }
}
