package com.tokyo.train.engine;

import com.tokyo.train.model.entity.TrainDelayEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DelayParser {

    /**
     * 核心方法：拉起一条管道，把硬盘文件里的文本，全自动变成 Java 对象的集合
     *
     * @param filePath 硬盘文件的相对路径
     * @return 洗干净后的电车数据集合
     */
    public static List<TrainDelayEntity> parseDelayFile(String filePath) {
        List<TrainDelayEntity> list = new ArrayList<>();

        // 🌟 缝合点一：穿上自动关流战甲，高级缓冲流 BufferedReader 套在 FileReader 外面
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // 🌟 缝合点二：利用 readLine() 一行一行往下吸，直到吸到文件末尾（null）
            while ((line = br.readLine()) != null) {

                // 防御卡点一：万一读到彻底的空行，直接跳过
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 🌟 核心切片：文本是“山手线,延迟,15,人身事故”，用逗号切碎成 4 个零件
                // 切完后：parts[0]="山手线", parts[1]="延迟", parts[2]="15", parts[3]="人身事故"
                String[] parts = line.split(",");

                // 防御卡点二：严防残缺数据。一行必须切出 4 个零件，否则就是脏数据，直接扔掉
                if (parts.length < 4) {
                    System.out.println("⚠️ 警报：扫描到残缺数据行，已全自动拦截 -> " + line);
                    continue;
                }

                // 提取零件
                String lineName = parts[0].trim();
                String status = parts[1].trim();
                String reason = parts[3].trim();

                // 🚨 核心避坑：文本里的 "15" 是字符串，但我们的模型里 delayMinutes 是 int！
                // 必须调用 Integer.parseInt() 把文本降维打碎成真正的数字！
                int delayMinutes = 0;
                try {
                    delayMinutes = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ 警报：延误分钟数格式错误，已强制归零 -> " + parts[2]);
                }

                // 防御卡点三：利用你前天写出来的逻辑，校验名字是否为空
                if (lineName.isEmpty()) {
                    System.out.println("🛡️ [安全拦截] 发现匿名违规线路数据，已成功阻断！");
                    continue;
                }

                // 🌟 奇迹诞生：把切碎并校验通过的零件，啪的一下拼装成一个活生生的 TrainDelayInfo 对象
                TrainDelayEntity info = new TrainDelayEntity(lineName, status, delayMinutes, reason);

                // 扔进总箩筐
                list.add(info);
            }

        } catch (IOException e) {
            System.out.println("🚨 致命错误：管道破裂，无法读取硬盘文件！" + e.getMessage());
        }

        return list;
    }
}
