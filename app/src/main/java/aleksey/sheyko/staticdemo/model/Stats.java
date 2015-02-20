package aleksey.sheyko.staticdemo.model;

import com.orm.SugarRecord;

public class Stats extends SugarRecord<Stats> {

    int mEntryId;
    String mService;
    String mUsername;
    String mLabel1;
    String mLabel2;
    String mLabel3;
    int mValue1;
    int mValue2;
    int mValue3;

    public Stats() {
    }

    public Stats(int entryId, String service, String username, String label1, int value1, String label2, int value2, String label3, int value3) {
        mEntryId = entryId;
        mService = service;
        mUsername = username;
        mLabel1 = label1;
        mLabel2 = label2;
        mLabel3 = label3;
        mValue1 = value1;
        mValue2 = value2;
        mValue3 = value3;
    }
}
