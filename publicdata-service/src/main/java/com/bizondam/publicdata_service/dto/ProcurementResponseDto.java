//package com.bizondam.publicdata_service.dto;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.Data;
//import java.util.List;
//
//@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class ProcurementResponseDto {
//
//    private Header header;
//    private Body   body;
//
//    @Data
//    public static class Header {
//        private String resultCode;
//        private String resultMsg;
//    }
//
//    @Data
//    public static class Body {
//        private List<ProcurementItemDto> items;
//        private int    numOfRows;
//        private int    pageNo;
//        private int    totalCount;
//    }
//}
