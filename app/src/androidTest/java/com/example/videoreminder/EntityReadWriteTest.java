package com.example.videoreminder;


import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.videoreminder.db.TaskRoomDatabase;
import com.example.videoreminder.db.dao.TaskDao;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class EntityReadWriteTest {
    private TaskDao dao;
    private TaskRoomDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, TaskRoomDatabase.class).build();
        dao = db.dao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


}
