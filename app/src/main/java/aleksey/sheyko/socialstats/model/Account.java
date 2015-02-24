package aleksey.sheyko.socialstats.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

    private int mId;
    private String mService;
    private String mUsername;
    private String mAvatarUri;
    private List<Stats> mStatsList;
    private String mAccessToken;

    public Account(String service, String username, List<Stats> statsList, String avatarUri, String accessToken) {
        mService = service;
        mUsername = username;
        mStatsList = statsList;
        mAvatarUri = avatarUri;
    }

    public Account(int id, String service, String username, List<Stats> statsList, String avatarUri, String accessToken) {
        mId = id;
        mService = service;
        mUsername = username;
        mStatsList = statsList;
        mAvatarUri = avatarUri;
        mAccessToken = accessToken;
    }

    public int getId() {
        return mId;
    }

    public String getService() {
        return mService;
    }

    public String getUsername() {
        return mUsername;
    }

    public List<Stats> getStatsList() {
        return mStatsList;
    }

    public void setStatsList(ArrayList<Stats> statsList) {
        mStatsList = statsList;
    }

    public String getAvatarUri() {
        return mAvatarUri;
    }


    private int mShowingDataSet;

    public int getShowingDataSet() {
        return mShowingDataSet;
    }

    public void notifyDataSetChanged() {

        if (mShowingDataSet < mStatsList.size() - 1) {
            mShowingDataSet++;
        } else {
            mShowingDataSet = 0;
        }
    }
}
