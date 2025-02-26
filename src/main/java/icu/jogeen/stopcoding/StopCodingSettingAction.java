package icu.jogeen.stopcoding;

import com.alibaba.fastjson2.JSONObject;
import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.MessageType;
import icu.jogeen.stopcoding.data.DataCenter;
import icu.jogeen.stopcoding.data.SettingData;
import icu.jogeen.stopcoding.service.TimerService;
import icu.jogeen.stopcoding.ui.SettingDialog;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class StopCodingSettingAction extends AnAction implements ApplicationComponent, AppLifecycleListener {

    private static final Logger LOG = Logger.getInstance(StopCodingSettingAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {
        SettingDialog settingDialog = new SettingDialog();
        settingDialog.setVisible(true);
    }

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        NotificationGroup notificationGroup = new NotificationGroup("StopCoding", NotificationDisplayType.BALLOON, true);

        SettingData settings = new SettingData();
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.loadFields(settings);
        DataCenter.settingData = settings;

        // 读取用户配置
        JSONObject data = new JSONObject();
        try {
            data = JSONObject.parseObject(new String(Files.readAllBytes(Paths.get(SettingData.FILE_PATH))));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        settings.setOpen(Optional.ofNullable(data).map(item -> item.getBoolean("isOpen")).orElse(DataCenter.settingData.isOpen()));
        settings.setWorkTime(Optional.ofNullable(data).map(item -> item.getInteger("worTimeTFText")).orElse(DataCenter.settingData.getWorkTime()));
        settings.setRestTime(Optional.ofNullable(data).map(item -> item.getInteger("restTimeTFText")).orElse(DataCenter.settingData.getRestTime()));

        if (settings.isOpen()) {
            LOG.info("open timer");
            String notifyStr = TimerService.openTimer();
            Notification notification = notificationGroup.createNotification(notifyStr, MessageType.INFO);
            Notifications.Bus.notify(notification);
        }

    }
}
