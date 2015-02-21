package aleksey.sheyko.staticdemo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private int mId;
    private String mService;
    private String mUsername;
    private List<AccountStats> mStatsList;
    private String mAvatarUrl;
    private int mShowingDataSet;

    public Account(String service, String username, List<AccountStats> statsList, String avatarUrl) {
        mService = service;
        mUsername = username;
        mStatsList = statsList;
        mAvatarUrl = avatarUrl;
    }

    public Account(int id, String service, String username, List<AccountStats> statsList, String avatarUrl) {
        mId = id;
        mService = service;
        mUsername = username;
        mStatsList = statsList;
        mAvatarUrl = avatarUrl;
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

    public List<AccountStats> getStatsList() {
        return mStatsList;
    }

    public void setStatsList(ArrayList<AccountStats> statsList) {
        mStatsList = statsList;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

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
