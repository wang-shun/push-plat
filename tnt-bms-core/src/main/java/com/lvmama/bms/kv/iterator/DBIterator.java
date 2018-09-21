package com.lvmama.bms.kv.iterator;

/**
 * @author Robert HG (254963746@qq.com) on 12/13/15.
 */
public interface DBIterator<V> {

    boolean hasNext();

    V next();

}