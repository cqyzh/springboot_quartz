package com.yzh.demo.task;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 具体任务类
 * @author : yzh
 * @date : 2020-09-27 10:33
 **/
@DisallowConcurrentExecution //不允许多线程运行
@Component
public class HelloWorldJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        LOGGER.info("任务【{}】-【{}】执行开始了--：{}",key.getName(),key.getGroup(),new Date());
    }
}
