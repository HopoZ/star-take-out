package com.star.service;

import com.star.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportSerive {
    /**
     * 统计指定日期范围内的营业额数据
     * @param startDate
     * @param endDate
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate startDate, LocalDate endDate);
}
