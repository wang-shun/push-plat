package com.lvmama.bms.core.cluster.sync;

public interface SyncListener {
    void onSyncEvent(SyncEvent syncEvent);
}