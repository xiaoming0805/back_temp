package com.cennavi.core.common.dao;

import com.cennavi.core.common.PageResult;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbc的基础操作
 * @param <T> 要操作的bean
 */
public interface BaseDao<T> {

    void save(T entity);

    <E> void save(final E bean,final Class<E> entityClass);

    void update(T entity);

    void delete(T entity);

    void delete(Serializable id);

    void batchDelete(List<String> idList, Class entity, String... tables);

    /**
     * (truncate) 删除指定entity对应的表的所有记录，且不保留日志
     */
    void deleteAll();

    /**
     *(truncate) 删除指定entity对应的表的所有记录，且不保留日志
     * @param tablename 表名
     */
    void deleteAll(String tablename);
    void deleteAll(Class entity);

    void delete(Map<String, String> where);

    /**
     * 批量保存
     */
    void batchSave(List<T> list);

    /**
     * 批量保存 ()
     * @param list  要保存的记录
     * @param entityClass 字段和表名对应
     * @param table 表名 优先级最高
     * @param <E>
     */
    <E> void batchSave(final List<E> list, final Class<E> entityClass, String table);

    <E> void batchSave(final List<E> list, final Class<E> entityClass);

    /**
     * 未完成
     */
//    void batchUpdate(List<T> list);

    <E> void batchUpdate(List<E> list, Class<E> entry);

    T findById(Serializable id);

    /**
     * 查询所有
     * @return
     */
    List<T> findAll();

    /**
     * 查询所有指定entity对应的表的所有数据
     * @param entity 要查询的表对应的bean
     * @return
     */
    <E> List<E> findAll(Class<E> entity);

    /**
     *  执行sql，返回指定的泛型bean集合
     * @param sql 查询sql
     * @param entityClass 返回bean
     * @return
     */
    <E> List<E> query(String sql, Class<E> entityClass);

    /**
     * 分页查询
     * @param pageNo 页码，第几页
     * @param pageSize 每页的数量
     * @return
     */
    PageResult<T> findByPage(int pageNo, int pageSize);

    /**
     * 分页查询
     * @param pageNo 页码，第几页
     * @param pageSize 每页的数量
     * @param where 条件，例如：name='张三'，map.put("name ='","张三"); in 则为map.put("name in ","张三,李四")
     * @return PageResult
     */
    PageResult<T> findByPage(int pageNo, int pageSize, Map<String, String> where);

    /**
     * 分页查询
     * @param pageNo 页码，第几页
     * @param pageSize 每页的数量
     * @param orderby Map<String, String> 第一个是排序的字段，第二个是排序方式，例：map.put("name","desc");
     * @return PageResult
     */
    PageResult<T> findByPage(int pageNo, int pageSize, LinkedHashMap<String, String> orderby);


    /**
     * 分页查询
     * @param pageNo 页码，第几页
     * @param pageSize 每页的数量
     * @param where 条件，例如：name='张三'，map.put("name =","张三"); in 则为map.put("name in","张三,李四")
     * @param orderby Map<String, String> 第一个是排序的字段，第二个是排序方式，例：map.put("name","desc");
     * @return PageResult
     */
    PageResult<T> findByPage(int pageNo, int pageSize, Map<String, String> where,
                             LinkedHashMap<String, String> orderby);

    /**
     *
     * @param where Map  例如：name='张三'，则 map.put("name","张三") 或者 map.put("name","=张三")
     *                    例如：name in ('张三','李四')则map.put("name","in张三,李四");
     *                    例如：name like ('张三%')则map.put("name","like张三");
     * @param orderby Map<String, String> 第一个是排序的字段，第二个是排序方式，例：map.put("name","desc");
     * @return
     */
    List<T> findByConditons(Map<String, String> where, LinkedHashMap<String, String> orderby);

    /**
     * 校验指定表中是否有指定字段
     * @param tableName 表名
     * @param columnName 字段名
     * @return
     */
    boolean checkColumn(String tableName,String columnName);
    /**
     * 判断表是否存在 pg
     * @param tablename
     * @return
     */
    boolean isExist(String tablename);
    boolean isExist(Class classname);

    void execute(String sql);

    void execSQL(String sql);

    List<T> executeSql(String sql);
}
