package com.populstay.populife.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.populstay.populife.entity.OfflineLock;

import java.util.ArrayList;
import java.util.List;

/**
 * 静态单例模式的 OfflineLock 数据库工具类
 * 全局唯一实例，避免多次数据库连接冲突
 */
public class PopulifeDBUtil {
    // 数据库常量
    private static final String DB_NAME = "PopulifeDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "offline_lock";

    // 静态单例实例
    private static PopulifeDBUtil instance;

    // 数据库帮助类
    private DBHelper dbHelper;

    /**
     * 私有构造方法，防止外部创建实例
     */
    private PopulifeDBUtil(Context context) {
        // 使用应用上下文，避免内存泄漏
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    /**
     * 获取单例实例
     * @param context 上下文（建议使用Application Context）
     * @return 全局唯一的 OfflineLockDBUtil 实例
     */
    public static synchronized PopulifeDBUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PopulifeDBUtil(context);
        }
        return instance;
    }

    /**
     * 新增或更新数据（包含lockType）
     */
    public boolean save(OfflineLock lock) {
        if (lock == null || lock.getLockMac() == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("userId", lock.getUserId());
            values.put("lockMac", lock.getLockMac());
            values.put("lockName", lock.getLockName());
            values.put("lockDataJson", lock.getLockDataJson());
            values.put("lockType", lock.getLockType());

            return db.replace(TABLE_NAME, null, values) != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    /**
     * 按userId和lockMac组合查询
     */
    public List<OfflineLock> queryByUserAndMac(String userId, String lockMac) {
        List<OfflineLock> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // 构建查询条件
            StringBuilder whereClause = new StringBuilder();
            List<String> whereArgs = new ArrayList<>();

            if (userId != null) {
                whereClause.append("userId = ?");
                whereArgs.add(userId);
            }
            if (lockMac != null) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("lockMac = ?");
                whereArgs.add(lockMac);
            }

            db = dbHelper.getReadableDatabase();
            cursor = db.query(
                    TABLE_NAME,
                    null,
                    whereClause.length() > 0 ? whereClause.toString() : null,
                    whereArgs.size() > 0 ? whereArgs.toArray(new String[0]) : null,
                    null, null, null
            );

            while (cursor.moveToNext()) {
                OfflineLock lock = new OfflineLock();
                lock.setUserId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
                lock.setLockMac(cursor.getString(cursor.getColumnIndexOrThrow("lockMac")));
                lock.setLockName(cursor.getString(cursor.getColumnIndexOrThrow("lockName")));
                lock.setLockDataJson(cursor.getString(cursor.getColumnIndexOrThrow("lockDataJson")));
                lock.setLockType(cursor.getInt(cursor.getColumnIndexOrThrow("lockType")));
                list.add(lock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return list;
    }

    /**
     * 按lockType查询
     */
    public List<OfflineLock> queryByLockType(int lockType) {
        List<OfflineLock> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(
                    TABLE_NAME,
                    null,
                    "lockType = ?",
                    new String[]{String.valueOf(lockType)},
                    null, null, null
            );

            while (cursor.moveToNext()) {
                OfflineLock lock = new OfflineLock();
                lock.setUserId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
                lock.setLockMac(cursor.getString(cursor.getColumnIndexOrThrow("lockMac")));
                lock.setLockName(cursor.getString(cursor.getColumnIndexOrThrow("lockName")));
                lock.setLockDataJson(cursor.getString(cursor.getColumnIndexOrThrow("lockDataJson")));
                lock.setLockType(cursor.getInt(cursor.getColumnIndexOrThrow("lockType")));
                list.add(lock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return list;
    }

    /**
     * 按lockMac查询单个
     */
    public OfflineLock queryByMac(String lockMac) {
        List<OfflineLock> result = queryByUserAndMac(null, lockMac);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * 按userId查询
     */
    public List<OfflineLock> queryByUserId(String userId) {
        return queryByUserAndMac(userId, null);
    }

    /**
     * 查询所有
     */
    public List<OfflineLock> queryAll() {
        return queryByUserAndMac(null, null);
    }

    /**
     * 按userId和lockMac组合删除
     */
    public int deleteByUserAndMac(String userId, String lockMac) {
        if (userId == null && lockMac == null) {
            return 0; // 避免误删所有数据
        }

        SQLiteDatabase db = null;
        try {
            // 构建删除条件
            StringBuilder whereClause = new StringBuilder();
            List<String> whereArgs = new ArrayList<>();

            if (userId != null) {
                whereClause.append("userId = ?");
                whereArgs.add(userId);
            }
            if (lockMac != null) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("lockMac = ?");
                whereArgs.add(lockMac);
            }

            db = dbHelper.getWritableDatabase();
            return db.delete(
                    TABLE_NAME,
                    whereClause.toString(),
                    whereArgs.toArray(new String[0])
            );
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (db != null) db.close();
        }
    }

    /**
     * 按lockType删除
     */
    public int deleteByLockType(int lockType) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            return db.delete(
                    TABLE_NAME,
                    "lockType = ?",
                    new String[]{String.valueOf(lockType)}
            );
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (db != null) db.close();
        }
    }

    /**
     * 按lockMac删除
     */
    public boolean deleteByMac(String lockMac) {
        return deleteByUserAndMac(null, lockMac) > 0;
    }

    /**
     * 按userId删除
     */
    public int deleteByUserId(String userId) {
        return deleteByUserAndMac(userId, null);
    }

    /**
     * 清空所有数据
     */
    public void clearAll() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
    }

    /**
     * 数据库帮助类（内部类）
     */
    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // 初始化表结构，包含所有字段
            String createTableSql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "userId TEXT," +
                    "lockMac TEXT PRIMARY KEY," +
                    "lockName TEXT," +
                    "lockDataJson TEXT," +
                    "lockType INTEGER)";
            db.execSQL(createTableSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 初始化阶段无需升级逻辑
        }
    }
}



