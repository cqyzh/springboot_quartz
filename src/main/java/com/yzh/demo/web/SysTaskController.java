package com.yzh.demo.web;

import com.yzh.demo.entity.SysTask;
import com.yzh.demo.manager.QuartzManager;
import com.yzh.demo.service.ISysTakService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : yzh
 * @date : 2020-09-27 10:42
 **/
@RestController
@RequestMapping("job")
public class SysTaskController {

    @Autowired
    private ISysTakService sysTakService;
    @Autowired
    private QuartzManager quartzManager;

    /**
     * 添加任务
     * @param task
     * @return
     */
    @PostMapping("add")
    public String add(@RequestBody SysTask task) throws Exception {
        sysTakService.add(task);
        return "ok";
    }

    /**
     * 更新任务
     * @param id
     * @return
     */
    @PutMapping("updateCron/{id}")
    public String updateCron(@PathVariable Long id,@RequestParam  String cronExpression)  {
        SysTask sysTask = sysTakService.findById(id);
        if (null == sysTask){
            return "任务不存在";
        }
        sysTask.setCronExpression(cronExpression);
        sysTakService.updateCron(sysTask);
        return "ok";
    }

    /**
     * 删除任务
     * @param id
     * @return
     */
    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id) {
        SysTask sysTask = sysTakService.findById(id);
        if (null == sysTask){
            return "任务不存在";
        }
        sysTakService.delete(sysTask);
        return "ok";
    }


    /**
     * 暂停任务
     * @return
     */
    @GetMapping("pause/{id}")
    public String pause(@PathVariable Long id) throws SchedulerException {
        SysTask sysTask = sysTakService.findById(id);
        if (null == sysTask){
            return "任务不存在";
        }
        quartzManager.pauseJob(sysTask);
        return "ok";
    }

    @GetMapping("resume/{id}")
    public String  resume(@PathVariable Long id) throws SchedulerException {
        SysTask sysTask = sysTakService.findById(id);
        if (null == sysTask){
            return "任务不存在";
        }
        quartzManager.resumeJob(sysTask);
        return "ok";
    }

    /**
     * 查找所有的任务
     * @return
     * @throws SchedulerException
     */
    @GetMapping("findAllJob")
    public Object findAllJob() throws SchedulerException {
        return quartzManager.findAllJob();
    }
}
