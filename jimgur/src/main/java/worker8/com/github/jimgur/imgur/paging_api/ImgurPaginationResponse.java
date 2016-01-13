package worker8.com.github.jimgur.imgur.paging_api;

import android.util.Log;

import java.util.ArrayList;

public class ImgurPaginationResponse {
    private String status;

    private Data[] data;

    private String success;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + ", data = " + data + ", success = " + success + "]";
    }

    public ArrayList<Data> getImgurDataList() {
        if (data == null) {
            Log.w("ImgurPaginationResponse", "getImgurDataList: data is empty!");
            return null;
        }
        // each time, only 60 objects, O(n) should be fast enough to not worry about this
        ArrayList<Data> list = new ArrayList<Data>();
        for (int i = 0; i < data.length; i++) {
            list.add(data[i]);
        }
        return list;
    }
}