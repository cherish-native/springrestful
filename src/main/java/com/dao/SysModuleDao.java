package com.dao;

import com.entity.SysModule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/18
 */
public interface SysModuleDao extends CrudRepository<SysModule,Integer> {

        List<SysModule> findByFlag(int flag);
}
