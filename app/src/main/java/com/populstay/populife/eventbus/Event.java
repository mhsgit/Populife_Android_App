package com.populstay.populife.eventbus;

public class Event {

    public int type;
    public Object obj;

    public Event(int type) {
        this.type = type;
    }

    public Event(int type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public interface EventType {
        // 新建空间
        int ADD_SPACE = 1;
        // 删除空间
        int DELETE_SPACE = 2;
        // 修改空间名称
        int RENAME_SPACE = 3;
        // 获取家庭组数据完成
        int GET_HOME_DATA_COMPLETE = 4;
        // 选择家庭组变化
        int CHANGE_HOME = 5;
        // 锁头本地初始化
        int LOCK_LOCAL_INITIALIZE_SUCCEED = 6;
        // 用户头像更新
        int USER_AVATAR_MODIFY = 7;
        // 用户昵称更新
        int USER_NIKE_NAME_MODIFY = 8;
        // 创建蓝牙钥匙成功
        int CREATE_BT_KEY_SUCCESS = 9;
        // 创建键盘密码成功
        int CREATE_PWD_SUCCESS = 10;
        // 密码状态同步成功
        int SYN_PWD_INFO_SUCCESS = 11;
        // 检查异地登录
        int FRAGMENT_RESUME_CHECK_REMOTE_LOGIN = 12;
        // 添加锁提交后台失败后，用于重置锁
        int INIT_LOCK_FAIL_RESET_WHEN_CONNECT = 13;
        // 锁头本地初始化失败
        int LOCK_LOCAL_INITIALIZE_FAIL = 14;
        // 是否展示管理员密码到密码列表中的开关发生变化
        int SHOW_LOCK_ADMIN_CODE_CONFIG_CHANGE = 15;
        // 清空钥匙
        int CLEAR_KEYS = 16;
        // 清空密码
        int CLEAR_PWDS = 17;
        //  失效钥匙
        int INVALIDATE_KEY = 18;
        // 还原钥匙（使重新生效）
        int RESTORE_KEY = 19;
        // 修改开锁密码(管理员密码)
        int MODIFY_LOCK_ADMIN_PASSCODE = 20;
        // 修改开锁密码(普通密码)
        int MODIFY_LOCK_PASSCODE = 21;
        // 修改开锁密码(管理员密码)
        int MODIFY_LOCK_ADMIN_PASSCODE_NAME = 22;
        // 修改开锁密码(普通密码)
        int MODIFY_LOCK_PASSCODE_NAME = 23;
        // 删除密码
        int DELETE_PWD = 24;
        // 修改钥匙有效期
        int MODIFY_KEY_PERIOD = 25;
        // 修改密码有效期
        int MODIFY_PWD_PERIOD = 26;
        // 添加设备完成
        int ADD_DEVICE_SUCCESS = 27;
        // 删除锁
        int DELETE_LOCK_SUCCESS = 28;
        // 添加门卡成功
        int ADD_IC_CARD_SUCCESS = 29;
        // 添指纹卡成功
        int ADD_FINGERPRINT_SUCCESS = 30;
        // 添指纹卡成功
        int PERMISSION_CHANGED = 31;
    }
}
