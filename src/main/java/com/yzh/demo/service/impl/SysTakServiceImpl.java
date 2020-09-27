package com.yzh.demo.service.impl;

import com.yzh.demo.dao.ISysTaskDao;
import com.yzh.demo.entity.SysTask;
import com.yzh.demo.enums.JobStatusEnum;
import com.yzh.demo.manager.QuartzManager;
import com.yzh.demo.service.ISysTakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : yzh
 * @date : 2020-09-27 09:56
 **/
@Service
public class SysTakServiceImpl implements ISysTakService {

    @Autowired
    private ISysTaskDao sysTaskDao;
    @Autowired
    private QuartzManager quartzManager;

    @Transactional(readOnly = true,propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void initSchedule() throws Exception {
        //获取任务信息
        List<SysTask> all = sysTaskDao.findAll();
        /**
         * 正在运行的
         */
        for (SysTask sysTask : all) {
            if (JobStatusEnum.RUNNING.getStatus().equals(sysTask.getJobStatus())){
                quartzManager.addJob(sysTask);
            }
        }
    }

    @Transactional
    @Override
    public void add(SysTask task) {
        sysTaskDao.save(task);
        if (JobStatusEnum.RUNNING.getStatus().equals(task.getJobStatus())){
            try {
                quartzManager.addJob(task);
            }catch (Exception e){
                throw new RuntimeException("任务添加失败：",e);
            }
        }
    }

    @Transactional
    @Override
    public void updateCron(SysTask task) {
        sysTaskDao.save(task);
        if (JobStatusEnum.RUNNING.getStatus().equals(task.getJobStatus())){
            try {
               quartzManager.updateJonCron(task);
            }catch (Exception e){
              throw new RuntimeException("修改corn表达式失败：",e);
            }
        }

    }

    @Transactional(readOnly = true,propagation = Propagation.NOT_SUPPORTED)
    @Override
    public SysTask findById(Long id) {
        return sysTaskDao.findById(id).get();
    }

    @Transactional
    @Override
    public void delete(SysTask task)  {
        sysTaskDao.deleteById(task.getId());
        if (JobStatusEnum.RUNNING.getStatus().equals(task.getJobStatus())){
            try {
                quartzManager.deleteJob(task);
            }catch (Exception e){
                throw new RuntimeException("删除失败：",e);
            }

        }
    }
}
