package com.example.springbootdemo;

public class MacOrder {
        private Integer id;
        private String food_name;
        private Integer order_number;
        private Integer quantity;
        private String remark;

        // ==================== 下面是标准老规矩：Getter / Setter ====================
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getFood_name() { return food_name; }
        public void setFood_name(String name) { this.food_name = name; }

        public Integer getOrder_number() { return order_number; }
        public void setOrder_number(Integer hourly_wage) { this.order_number = hourly_wage; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer position) { this.quantity = position; }

        public String getRemark() { return remark; }
        public void setRemark(String status) { this.remark = status; }
}
