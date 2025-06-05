package com.bizondam.publicdata_service.mapper;

import com.bizondam.publicdata_service.domain.ProcurementContract;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcurementContractMapper {

    // XML <insert id="upsert">에 매핑
//    void upsert(ProcurementContract c);

    // XML <select id="selectByRequestNo">에 매핑
    ProcurementContract selectByRequestNo(String requestNo);

    List<ProcurementContract> selectAll();
}
