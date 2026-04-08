package com.example.demo2.enums;

import lombok.Getter;

@Getter
public enum BillType {

    WATER("WATER", "水費分攤"),
    ELECTRICITY("ELECTRICITY", "電費分攤"),
    MANAGEMENTFEE("MANAGEMENTFEE", "管理費"),
    CAR_PARKINGCLEANINGFEE("CARPARKINGCLEANINGFEE", "汽車位清潔費"),
    LOCOMOTIVE_PARKINGCLEANINGFEE("LOCOMOTIVE_PARKINGCLEANINGFEE", "機車位清潔費"),
    OTHEREXPENSES("OTHEREXPENSES", "其他費用");

    private final String code;
    private final String displayName;

    // 建構子
    BillType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * 根據字串 Code 獲取對應的 Enum 物件
     * 常用於從前端傳入字串時進行轉換
     */
    public static BillType fromCode(String code) {
        for (BillType type : BillType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("不支援的帳單類型: " + code);
    }
}
