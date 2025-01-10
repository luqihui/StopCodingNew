package icu.jogeen.stopcoding.data;

import com.alibaba.fastjson2.JSONObject;
import com.intellij.ide.util.PropertyName;

import java.io.FileWriter;
import java.io.IOException;

public class SettingData {

    public static final int DEFAULT_WORK_TIME = 1;
    public static final int DEFAULT_REST_TIME = 2;

    @PropertyName("StopCoding:SettingData:isOpen")
    private boolean isOpen = false;

    @PropertyName("StopCoding:SettingData:workTime")
    private int workTime = DEFAULT_WORK_TIME;

    @PropertyName("StopCoding:SettingData:restTime")
    private int restTime = DEFAULT_REST_TIME;

    public static final String FILE_PATH = System.getProperty("user.home") + "/.stopcoding-idea-plugin-data.txt";

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public static SettingData of(boolean selected, int restTimeTFText, int worTimeTFText) {
        SettingData settings = new SettingData();
        settings.setRestTime(restTimeTFText);
        settings.setWorkTime(worTimeTFText);
        settings.setOpen(selected);

        // 持久化保存工作、休息间隔
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isOpen", selected);
        jsonObject.put("restTimeTFText", restTimeTFText);
        jsonObject.put("worTimeTFText", worTimeTFText);
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(jsonObject.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return settings;
    }



}
