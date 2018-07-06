package com.tony.tonythetiger.team_plane.Object;

import com.tony.tonythetiger.team_plane.ServerConnection.OnPostListener;
import com.tony.tonythetiger.team_plane.ServerConnection.SetData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tony on 2017-12-07.
 */

public class Team extends User {
    private String team_sign_in = "team_join.php";
    private OnPostListener onPostListener;
    public Team() {
    }

    // onPostListner을 통해 서버에서 데이터 받음
    public void SignIn(HashMap<String,String> parameters , OnPostListener onPostListener){
        this.onPostListener = onPostListener;
        // SetData 생성
        SetData signIn = new SetData(onPostListener);
        // 보낼 데이터 parameters 넘기기
        signIn.setParameters(parameters);
        // 가져올 데이터 parameter 설정
        String[] strings = {"keyy"};
        signIn.setJsonParameter(strings);
        // serverUrl 넘기고 실행
        signIn.execute(ServerURL+team_sign_in);

    }

    public void matching(HashMap<String, String> parameters, OnPostListener onPostListener){
        SetData matching = new SetData(onPostListener);
        matching.setParameters(parameters);
        String[] strings = {"keyy", "name","career_interest", "role", "career", "d", "i", "s", "c", "area"};
        matching.setJsonParameter(strings);
        matching.execute(ServerURL + matchingPHP);
        this.onPostListener = onPostListener;
    }
}
