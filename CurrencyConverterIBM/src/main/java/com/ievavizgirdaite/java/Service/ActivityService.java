package com.ievavizgirdaite.java.Service;

import com.ievavizgirdaite.java.Entity.Activity;
import com.ievavizgirdaite.java.Entity.Repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository activityRepository;

    @Transactional
    public void add(Activity activity){
        activityRepository.save(activity);
    }

    public Activity createActivity(String method, String date)
    {
        Activity activity = new Activity();
        activity.setDate(date);
        activity.setMethod(method);
        return activity;
    }


}
