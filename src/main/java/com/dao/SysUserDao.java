package com.dao;

import com.entity.SysUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/19
 */
public interface SysUserDao extends CrudRepository<SysUser,Integer> {

    Optional<SysUser> findByUserNameAndFlag(String username,int flag);

    List<SysUser> findByFlag(int flag);

    @Transactional
    void deleteById(String id);
}
