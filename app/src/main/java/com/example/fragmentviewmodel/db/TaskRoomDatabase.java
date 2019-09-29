package com.example.fragmentviewmodel.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.fragmentviewmodel.db.dao.TaskDao;
import com.example.fragmentviewmodel.db.entity.NotificationTask;

@Database(entities = {NotificationTask.class}, version = 3, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {

    public abstract TaskDao dao();

    private static TaskRoomDatabase INSTANCE;

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TaskDao dao;

        PopulateDbAsync(TaskRoomDatabase db) {
            dao = db.dao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            dao.deleteAll();

            NotificationTask task = new NotificationTask("Read book", "Description ", 1);
            dao.insert(task);
            task = new NotificationTask("Buy plane tickets", "Description ", 1);
            dao.insert(task);
            task = new NotificationTask("Go to gym", "Description ", 2);
            dao.insert(task);
            task = new NotificationTask("Clean the house", "Description ", 3);
            dao.insert(task);
            task = new NotificationTask("Buy groceries", "Description ", 2);
            dao.insert(task);

            return null;
        }
    }

    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, "task_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
