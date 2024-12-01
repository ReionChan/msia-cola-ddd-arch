package io.github.reionchan.core.util;

import com.alibaba.cola.dto.PageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import static com.alibaba.cola.dto.PageQuery.ASC;
import static io.github.reionchan.core.util.CommonUtil.cleanIdentifier;

/**
 * @author Reion
 * @date 2024-11-26
 **/
public class ConditionUtil {
    public static <T> IPage<T> getPage(PageQuery query) {
        Page<T> page = new Page<>(query.getPageIndex(), query.getPageSize());
        String[] columnArr = CommonUtil.toStrArray(query.getOrderBy());
        if (ASC.equals(query.getOrderDirection())) {
            for (String asc : columnArr) {
                page.addOrder(OrderItem.asc(cleanIdentifier(asc)));
            }
        } else {
            for (String desc : columnArr) {
                page.addOrder(OrderItem.desc(cleanIdentifier(desc)));
            }
        }
        return page;
    }
}
