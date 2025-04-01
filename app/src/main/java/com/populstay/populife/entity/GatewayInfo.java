package com.populstay.populife.entity;

import java.util.List;

public class GatewayInfo {
    private boolean hasGateway;
    private List<Gateway> dataList;

    public GatewayInfo(boolean hasGateway, List<Gateway> dataList) {
        this.hasGateway = hasGateway;
        this.dataList = dataList;
    }

    public boolean isHasGateway() {
        return hasGateway;
    }

    public void setHasGateway(boolean hasGateway) {
        this.hasGateway = hasGateway;
    }

    public List<Gateway> getDataList() {
        return dataList;
    }

    public void setDataList(List<Gateway> dataList) {
        this.dataList = dataList;
    }
}
