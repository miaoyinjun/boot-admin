package org.jjche.mybatis.base.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jjche.mybatis.base.MyBaseMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
 * <p>
 * 自定义service实现
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-26
 */
public class MyServiceImpl<M extends MyBaseMapper<T>, T> extends ServiceImpl<M, T>
        implements IMyService<T> {
    /**
     * 采用insertBatchSomeColumn重写saveBatch方法，保留分批处理机制
     *
     * @param entityList /
     * @param batchSize  /
     * @return 成功
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean fastSaveBatch(Collection<T> entityList, int batchSize) {
        try {
            int size = entityList.size();
            int idxLimit = Math.min(batchSize, size);
            int i = 1;
            //保存单批提交的数据集合
            List<T> oneBatchList = new ArrayList<>();
            for (Iterator<T> var7 = entityList.iterator(); var7.hasNext(); ++i) {
                T element = var7.next();
                oneBatchList.add(element);
                if (i == idxLimit) {
                    baseMapper.insertBatchSomeColumn(oneBatchList);
                    //每次提交后需要清空集合数据
                    oneBatchList.clear();
                    idxLimit = Math.min(idxLimit + batchSize, size);
                }
            }
        } catch (Exception e) {
            log.error("saveBatch fail", e);
            return false;
        }
        return true;
    }
}
