package com.lvmama.bms.bridge.callback;

import java.util.List;

public abstract class TokenCallback {

    private Object tokenBindService;

    public TokenCallback(Object tokenBindService) {
        this.tokenBindService = tokenBindService;
    }

    public abstract List<String> prepare();

}
