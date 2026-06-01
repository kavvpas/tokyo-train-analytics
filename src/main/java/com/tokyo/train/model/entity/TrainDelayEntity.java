package com.tokyo.train.model.entity;

public class TrainDelayEntity {
    // 🌟 大厂规范：Entity 必须拥有跟数据库一模一样的自增主键 id！
    private Integer id;
    private String lineName;
    private String status;
    private Integer delayMinutes;
    private String reason;

    // 空构造方法
    public TrainDelayEntity() {}

    // 全参数构造方法
    public TrainDelayEntity(Integer id, String lineName, String status, Integer delayMinutes, String reason) {
        this.id = id;
        this.lineName = lineName;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
    }

    // 快捷构造方法（方便爬虫拼装，不带ID）
    public TrainDelayEntity(String lineName, String status, Integer delayMinutes, String reason) {
        this.lineName = lineName;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
    }

    // 标准的 Getter 和 Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(Integer delayMinutes) { this.delayMinutes = delayMinutes; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}