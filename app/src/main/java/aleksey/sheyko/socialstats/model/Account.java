package aleksey.sheyko.socialstats.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private int mId;
    private String mService;
    private String mUsername;
    private List<DataSet> mStatsList;
    private String mAvatarUrl;
    private int mShowingDataSet;

    public Account(String service, String username, List<DataSet> statsList, String avatarUrl) {
        mService = service;
        mUsername = username;
        mStatsList = statsList;
        mAvatarUrl = avatarUrl;
    }

    public Account(int id, String service, String username, List<DataSet> statsList, String avatarUrl) {
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

    public List<DataSet> getStatsList() {
        return mStatsList;
    }

    public void setStatsList(ArrayList<DataSet> statsList) {
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
