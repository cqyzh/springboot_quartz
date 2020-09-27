package com.yzh.demo.dao;

import com.yzh.demo.entity.SysTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author : yzh
 * @date : 2020-09-27 09:54
 **/
@Component
public interface ISysTaskDao extends JpaRepository<SysTask,Long> {
}
