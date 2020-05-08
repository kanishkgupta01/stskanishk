package com.thinking.machine.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thinking.machine.model.mainuser;



@Repository
public interface mainrepo extends CrudRepository<mainuser, Long> {
	
	public mainuser findByUserName(String userName);

}
