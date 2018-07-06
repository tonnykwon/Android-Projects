package com.tony.tonythetiger.team_plane.Object;

import com.tony.tonythetiger.team_plane.ServerConnection.OnPostListener;
import com.tony.tonythetiger.team_plane.ServerConnection.SetData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tony on 2017-12-04.
 */

public class Individual extends User {
    private String individual_sign_in = "user_join.php";
    private OnPostListener onPostListener;

    public Individual() {
}

    public void SignIn(HashMap<String,String> parameters, OnPostListener onPostListener){
        this.onPostListener = onPostListener;
        SetData signIn = new SetData(onPostListener);
        signIn.setParameters(parameters);
        String[] strings = {"keyy"};
        signIn.setJsonParameter(strings);
        // serverUrl 넘기고 실행
        signIn.execute(ServerURL+individual_sign_in);
    }
    public void matching(HashMap<String, String> parameters, OnPostListener onPostListener){
        SetData matching = new SetData(onPostListener);
        matching.setParameters(parameters);
        String[] strings = {"keyy","name", "field", "finding_role", "explanation", "d", "i", "s", "c", "area"};
        matching.setJsonParameter(strings);
        matching.execute(ServerURL + matchingPHP);
        this.onPostListener = onPostListener;
    }

}
