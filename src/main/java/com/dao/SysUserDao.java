package com.dao;

import com.entity.SysUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/19
 */
public interface SysUserDao extends CrudRepository<SysUser,Integer> {

    Optional<SysUser> findByUserNameAndFlag(String username,int flag);
}
