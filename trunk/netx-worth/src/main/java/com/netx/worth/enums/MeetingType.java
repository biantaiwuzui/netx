package com.netx.worth.enums;

public enum MeetingType {
	HAS_ONE(1, "活动，即1对1"),
    HAS_MANY(2, "聚合，即多对多"),
    ONLINE(3, "纯线上活动"),
    OFFLINE_NO_FEE(4, "不发生消费的线下活动");
    public Integer type;
    private String description;

    private MeetingType(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
