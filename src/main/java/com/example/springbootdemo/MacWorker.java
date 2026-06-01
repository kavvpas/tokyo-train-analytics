package com.example.springbootdemo;

public class mac_worker {
        private Integer id;
        private String name;
        private Integer hourly_wage;
        private String position;
        private String status;

        // ==================== 下面是标准老规矩：Getter / Setter ====================
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getHourly_wage() { return hourly_wage; }
        public void setHourly_wage(Integer hourly_wage) { this.hourly_wage = hourly_wage; }

        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
}
