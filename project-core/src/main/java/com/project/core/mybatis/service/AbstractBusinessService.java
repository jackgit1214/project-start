package com.project.core.mybatis.service;

import com.project.core.mybatis.dao.Base.IDataMapper;
import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.core.mybatis.dao.Base.IDataMapperWithBlob;
import com.project.core.mybatis.model.CommSeq;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.dao.Base.CommSeqMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class AbstractBusinessService<T> implements IBusinessService<T>, InitializingBean {

    protected int DEFAULTROWNUMPERPAGE = 10;

    @Autowired
    private CommSeqMapper commSeqMapper;

    private IDataMapper<T> dataMapper;
    private IDataMapperWithBlob<T> dataMapperWithBlob;
    private IDataMapperByPage<T> dataMapperByPage;
    private IDataMapperCRUD<T> dataMapperCRUD;

    protected final Log log = LogFactory.getLog(this.getClass());
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    SimpleKeyGenerator k = new SimpleKeyGenerator();

    @Cacheable(value = "CustomerCache", key = "#root.target")
    public List<T> findAllObjects() {
        if (this.dataMapper != null)
            return this.dataMapper.selectByCondition(null);
        else if (this.dataMapperCRUD != null)
            return this.dataMapperCRUD.selectByCondition(null);
        else {
            this.log.debug("未实现dataMapper或dataMapperCRUD接口");
            return null;
        }
    }

    @Override
    public T findObjectById(Object object) {
        if (this.dataMapper != null)
            return this.dataMapper.selectByPrimaryKey(object);
        else if (this.dataMapperCRUD != null)
            return this.dataMapperCRUD.selectByPrimaryKey(object);
        else {
            this.log.debug("未实现dataMapper或dataMapperCRUD接口");
            return null;
        }
    }

    @Override
    @Cacheable(value = "CustomerCache", key = "#queryModel.condition")
    public List<T> findObjects(QueryModel queryModel) {
        if (this.dataMapper != null)
            return this.dataMapper.selectByCondition(queryModel);
        else if (this.dataMapperCRUD != null)
            return this.dataMapperCRUD.selectByCondition(queryModel);
        else {
            this.log.debug("未实现dataMapper或dataMapperCRUD接口");
            return null;
        }
    }

    public T singleObject(QueryModel queryModel) throws Exception {

        List<T> datas = this.findObjects(queryModel);
        if (datas.size() != 1) {
            throw new Exception("您得到的数据不为1;");
        }
        return datas.get(0);
    }

    @Override
    public PageResult<T> findObjectsByPage(QueryModel queryModel, PageResult<T> page) throws Exception {
        if (this.dataMapperByPage == null)
            throw new Exception("未实现分页接口");

        List<T> pageDatas = this.dataMapperByPage.selectByCondition(queryModel, page);
        page.setPageDatas(pageDatas);
        return page;
    }

    public PageResult<T> findObjectsByPage(IDataMapper baseDao, String methodName, QueryModel queryModel,
                                           PageResult<T> page) throws Exception {
//        Proxy.newProxyInstance(this.dataMapper.getClass().getClassLoader(), new Class[]{},
//                new MapperProxyHandler(baseDao));

        List<T> pageDatas = this.dataMapperByPage.selectByCondition(queryModel, page);
        page.setPageDatas(pageDatas);
        return page;
    }

    //@PostConstruct
    public abstract BaseDao getDao();

    @SuppressWarnings("unchecked")
    //@PostConstruct
    private void initDataMapper() {
        BaseDao baseDao = this.getDao();
        if (baseDao instanceof IDataMapper) {
            this.dataMapper = (IDataMapper<T>) baseDao;
        }
        if (baseDao instanceof IDataMapperWithBlob)
            this.dataMapperWithBlob = (IDataMapperWithBlob<T>) baseDao;

        if (baseDao instanceof IDataMapperByPage) {
            this.dataMapperByPage = (IDataMapperByPage<T>) baseDao;
        }

        if (baseDao instanceof IDataMapperCRUD) {
            this.dataMapperCRUD = (IDataMapperCRUD<T>) baseDao;
        }
    }

    class MapperProxyHandler implements InvocationHandler {
        private Object proxied;

        public MapperProxyHandler(Object proxied) {
            this.proxied = proxied;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(proxied, args);
        }
    }

    @Override
    public synchronized int getNextVal(String sequenceName) {
        Map<String, Integer> map = this.commSeqMapper.getNextVal(sequenceName);
        int seqValue = map.get("value");
        if (seqValue == 0) { //为0时，新增序列名
            CommSeq comSeq = new CommSeq();
            comSeq.setSequenceName(sequenceName);
            comSeq.setCurrentVal(seqValue);
            comSeq.setIncrementVal(1);
            this.commSeqMapper.insert(comSeq);
        }
        return seqValue;
    }

    @Override
    public int getCurVal(String sequenceName) {

        Map<String, Integer> map = this.commSeqMapper.getCurrVal(sequenceName);

        // TODO Auto-generated method stub
        return map.get("value");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initDataMapper();
    }
}
