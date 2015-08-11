package com.example.yifei.mobilesafe;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.example.yifei.mobilesafe.db.dao.BlackNumberDao;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public Context context;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        context = getContext();
        super.setUp();
    }

    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(context);
        Random random = new Random();
        for (int i=0;i<100;i++){
            Long number = 13111111111l + i;
            dao.add(number + "", String.valueOf(random.nextInt(3)+1));
        }
    }

    public void testDelete(){
        BlackNumberDao dao = new BlackNumberDao(context);
        boolean delete = dao.delete("1300000000");
        assertEquals(true,delete);
    }

    public void testFind(){
        BlackNumberDao dao = new BlackNumberDao(context);
        String mode = dao.findNumber("1300000001");
        assertEquals("2",mode);
    }

    public void testUpdate(){
        BlackNumberDao dao = new BlackNumberDao(context);
        boolean update = dao.change("1300000001","3");
        String mode = dao.findNumber("1300000001");
        assertEquals("3",mode);


    }
}