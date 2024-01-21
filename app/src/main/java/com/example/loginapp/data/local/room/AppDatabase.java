package com.example.loginapp.data.local.room;

import androidx.room.RoomDatabase;

//@Database(entities = {Product.class}, version = 1, exportSchema = false)
abstract public class AppDatabase extends RoomDatabase {
//    public abstract AppDao dao();
//
//    private static AppDatabase INSTANCE = null;
//
//    public static AppDatabase getInstance(Context context) {
//        if (INSTANCE == null) {
//            synchronized (AppDatabase.class) {
//                AppDatabase instance = Room.databaseBuilder(
//                    context.getApplicationContext(),
//                    AppDatabase.class,
//                    "app_db"
//                ).build();
//                INSTANCE = instance;
//                return instance;
//            }
//        }
//        return INSTANCE;
//    }
}
