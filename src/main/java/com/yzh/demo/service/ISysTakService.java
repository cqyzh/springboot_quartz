package com.yzh.demo.service;

import com.yzh.demo.entity.SysTask;

/**
 * @author : yzh
 * @date : 2020-09-27 09:56
 **/
public interface ISysTakService {

    void initSchedule() throws Exception;

    void add(SysTask task) throws Exception;

    void updateCron(SysTask task);

    SysTask findById(Long id);

    void delete(SysTask sysTask);
}
