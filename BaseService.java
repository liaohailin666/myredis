package com.mytaotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mytaotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {

	//获取mapper
	@Autowired
	public  Mapper<T> mapper;
	
	//根据id查询数据
	public T queryById(Long id){
		return this.mapper.selectByPrimaryKey(id);
	}
	
	//查询所有数据
	public List<T> queryAll(){
		return this.mapper.select(null);
	}
	//根据条件查询一条数据
	public T queryOne(T  t){
		return this.mapper.selectOne(t);
	}
	
	//根据条件查询数据列表
	public List<T> queryListByWhere(T t){
		return this.mapper.select(t);
	}
	
	//分页查询
	public PageInfo<T> queryPageListByWhere(Integer page,Integer rows,T t){
		PageHelper.startPage(page, rows);
		return new PageInfo<T>(this.mapper.selectByExample(t));
	}
	
	//新增数据 返回成功的条数
	public Integer save(T t){
		return this.mapper.insert(t);
	}
	
	//新增数据，使用不为null的字段，返回成功的条数
	public Integer saveSelective(T t){
		t.setCreated(new Date());
		t.setUpdated(t.getCreated());
		return this.mapper.insertSelective(t);
	}
	 //修改数据，返回成功的条数
	public Integer update(T t){
		return this.mapper.updateByPrimaryKey(t);
	}
	
	//修改数据，使用不为null的字段，返回成功的条数
	public Integer updateSelective(T t){
		return this.mapper.updateByPrimaryKeySelective(t);
	}
	
	//根据id删除数据
	public Integer deleteById(Long id){
		return this.mapper.deleteByPrimaryKey(id);
	}
	
	//批量删除
	public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values);
        return this.mapper.deleteByExample(example);
    }
	
	//根据条件做删除
	public Integer deleteByWhere(T t){
		return this.mapper.delete(t);
	}
}
