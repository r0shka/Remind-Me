package com.example.videoreminder.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.videoreminder.db.dao.TaskDao;
import com.example.videoreminder.db.entity.Task;

@Database(entities = {Task.class}, version = 6, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {

    private static TaskRoomDatabase INSTANCE;
    public abstract TaskDao dao();

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

            Task task = new Task("Read book", "Description ", Task.VIDEO_TYPE_TASK);
            dao.insert(task);
            task = new Task("Buy plane tickets", "Description ", Task.VIDEO_TYPE_TASK);
            dao.insert(task);
            task = new Task("Go to gym", "Description ", Task.AUDIO_TYPE_TASK);
            dao.insert(task);
            task = new Task("Clean the house", "Description ", Task.TEXT_TYPE_TASK);
            dao.insert(task);
            task = new Task("Buy groceries", "Description ", Task.AUDIO_TYPE_TASK);
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
