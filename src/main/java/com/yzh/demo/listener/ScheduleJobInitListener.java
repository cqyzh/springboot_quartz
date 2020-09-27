package com.yzh.demo.listener;

import com.yzh.demo.service.ISysTakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *监听器
 * @author : yzh
 * @date : 2020-09-27 10:03
 **/
@Component
@Order(1)
public class ScheduleJobInitListener implements CommandLineRunner {

    @Autowired
    private ISysTakService takService;

    @Override
    public void run(String... args) throws Exception {
        takService.initSchedule();
    }
}
