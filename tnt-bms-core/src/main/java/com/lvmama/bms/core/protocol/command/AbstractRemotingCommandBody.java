package com.lvmama.bms.core.protocol.command;

import com.lvmama.bms.core.support.SystemClock;
import com.lvmama.bms.remoting.annotation.NotNull;
import com.lvmama.bms.remoting.annotation.Nullable;
import com.lvmama.bms.remoting.exception.RemotingCommandFieldCheckException;
import com.lvmama.bms.remoting.RemotingCommandBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Robert HG (254963746@qq.com) on 7/24/14.
 * 抽象的header 传输信息
 */
public class AbstractRemotingCommandBody implements RemotingCommandBody {

	private static final long serialVersionUID = -8184979792935677160L;

	/**
     * 节点组 当前节点的 group(统一类型, 具有相同功能的节点group相同)
     */
    @NotNull
    private String nodeGroup;

    /**
     * NodeType 的字符串表示, 节点类型
     */
    @NotNull
    private String nodeType;

    /**
     * 当前节点的唯一标识
     */
    @NotNull
    private String identity;

    private Long timestamp = SystemClock.now();

    // 额外的参数
    @Nullable
    private Map<String, Object> extParams;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public Map<String, Object> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, Object> extParams) {
        this.extParams = extParams;
    }

    public void putExtParam(String key, Object obj) {
        if (this.extParams == null) {
            this.extParams = new HashMap<String, Object>();
        }
        this.extParams.put(key, obj);
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public void checkFields() throws RemotingCommandFieldCheckException {

    }
}
