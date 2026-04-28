package com.example.demo2.enums;

public enum FinancailLedgerCategory {
	// --- 支出類 ---
    SALARY,          // 員工薪資
    REPAIR,          // 維修費 (水電、電梯、弱電)
    PURCHASE,        // 採購 (辦公用品、清潔耗材)
    WATER_ELEC,      // 公共水電費
    CLEANING,        // 清潔外包
    SECURITY,        // 保全物業費
    INSURANCE,       // 保險費
    
    // --- 收入類 ---
    MGMT_FEE,        // 管理費收入
    FACILITY_RENTAL, // 公設租金收入
    PENALTY,         // 罰金/違規金收入
    
    // --- 其他 ---
    COMMUNITY_ACT,   // 社區活動
    OTHER            // 其他
}
