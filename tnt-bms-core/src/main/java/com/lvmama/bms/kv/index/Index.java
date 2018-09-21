package com.lvmama.bms.kv.index;

import com.lvmama.bms.kv.iterator.DBIterator;
import com.lvmama.bms.kv.txlog.StoreTxLogPosition;
import com.lvmama.bms.kv.Entry;

/**
 * @author Robert HG (254963746@qq.com) on 12/18/15.
 */
public interface Index<K, V> {

    IndexItem<K> getIndexItem(K key);

    IndexItem<K> removeIndexItem(StoreTxLogPosition txLogResult, K key);

    void putIndexItem(StoreTxLogPosition txLogResult, K key, IndexItem<K> indexItem);

    public int size();

    public boolean containsKey(K key);

    DBIterator<Entry<K, V>> iterator();

    StoreTxLogPosition lastTxLog();
}
