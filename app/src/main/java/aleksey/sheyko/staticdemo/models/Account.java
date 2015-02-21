package aleksey.sheyko.staticdemo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private int mId;
    private String mService;
    private String mUsername;
    private List<AccountStats> mStatsList;
    private int mShowingDataSet;

    public Account(String service, String username, List<AccountStats> statsList) {
        mService = service;
        mUsername = username;
        mStatsList = statsList;
    }

    public Account(int id, String service, String username, List<AccountStats> statsList) {
        mId = id;
        mService = service;
        mUsername = username;
        mStatsList = statsList;
    }

    public int getId() {
        return mId;
    }

    public String getService() {
        return mService;
    }

    public void setService(String service) {
        mService = service;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public List<AccountStats> getStatsList() {
        return mStatsList;
    }

    public void setStatsList(ArrayList<AccountStats> statsList) {
        mStatsList = statsList;
    }

    public int getNextDataSet() {
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
