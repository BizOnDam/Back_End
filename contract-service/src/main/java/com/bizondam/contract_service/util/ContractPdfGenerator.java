package com.bizondam.contract_service.util;

import com.bizondam.contract_service.dto.ContractDto;
import com.bizondam.contract_service.dto.ContractItemDto;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractPdfGenerator {
    private static final String TEMPLATE_PATH = "templates/contract-template.html";
    private static final String FONT_PATH = "fonts/Pretendard-Regular.ttf";

    public byte[] generatePdf(ContractDto dto) throws IOException {
        log.info("PDF 생성 시작 - contractId={}, buyer={}, supplier={}",
                dto.getContractId(), dto.getBuyerCompanyName(), dto.getSupplierCompanyName());

        String html = buildContractHtml(dto);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  // 생성된 PDF를 바이트 배열로 저장할 메모리 스트림 생성
             InputStream fontStream = getClass().getClassLoader().getResourceAsStream(FONT_PATH)) {    // 폰트 가져오기

            if (fontStream == null) {
                throw new FileNotFoundException("Pretendard-Regular.ttf 폰트 파일을 찾을 수 없습니다.");
            }

            // InputStream을 임시 파일로 저장
            File tempFontFile = File.createTempFile("Pretendard-Regular", ".otf");  // OS의 임시 디렉토리에 빈 .otf 파일 생성
            try (FileOutputStream fos = new FileOutputStream(tempFontFile)) {
                fontStream.transferTo(fos); // 읽은 폰트 데이터를 임시 파일에 그대로 사용
            }

            // pdf 빌더
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();  // PDF 품질과 속도 균형 조절
            builder.useFont(tempFontFile, "Pretendard", 400, BaseRendererBuilder.FontStyle.NORMAL, true);   // 폰트 설정
            builder.withHtmlContent(html, null);    // 생성된 HTML 문자열을 PDF로 렌더링
            builder.toStream(outputStream); // 출력 스트림을 지정
            builder.run();  // PDF 생성 실행

            log.info("PDF 생성 완료 - contractId={}", dto.getContractId());
            return outputStream.toByteArray();  // 바이트로 반환

        } catch (Exception e) {
            log.error("PDF 생성 실패 - contractId={}, message={}", dto.getContractId(), e.getMessage(), e);
            throw new IOException("PDF 생성 중 오류 발생", e);
        }
    }

    private String buildContractHtml(ContractDto dto) throws IOException {
        log.info("HTML 템플릿 불러오기 시도 - 경로: {}", TEMPLATE_PATH);
        InputStream is = getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH);
        if (is == null) {
            log.error("템플릿 HTML 파일을 찾을 수 없습니다 - 경로: {}", TEMPLATE_PATH);
            throw new FileNotFoundException("템플릿 HTML 파일을 찾을 수 없습니다. 경로: " + TEMPLATE_PATH);
        }

        String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        log.info("템플릿 로딩 성공");

        // tbody 구성
        StringBuilder tableRows = new StringBuilder();
        int i = 1;
        for (ContractItemDto item : dto.getItems()) {
            long totalPrice = (long) item.getQuantity() * item.getUnitPrice();
            tableRows.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(item.getDetailCategoryName() != null ? item.getDetailCategoryName() : item.getSpecification()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("<td>").append(item.getSpecification() != null ? item.getSpecification() : item.getCategoryName()).append("</td>")
                    .append("<td>").append(formatPrice(item.getUnitPrice())).append("</td>")
                    .append("<td>").append(formatPrice(totalPrice)).append("</td>")
                    .append("<td>").append(item.getDeliveryDays()).append("일").append("</td>")
                    .append("</tr>");
        }

        log.debug("계약 품목 {}건 HTML 테이블 구성 완료", dto.getItems().size());

        return template
                .replace("[수요업체]", dto.getBuyerCompanyName())
                .replace("[공급업체]", dto.getSupplierCompanyName())
                .replace("[수요자의 지정 장소]", dto.getBuyerCompanyAddress() + " " + dto.getBuyerCompanyAddressDetail())
                .replace("[날짜]", formatDate(dto.getDueDate()))
                .replace("[금액]", formatPrice(dto.getTotalPrice()))
                .replace("[결제조건]", Optional.ofNullable(dto.getPaymentTerms()).orElse("-"))
                .replace("[1년]", Optional.ofNullable(dto.getWarranty()).orElse("-"))
                .replace("[홍길동]", dto.getBuyerUserName())
                .replace("[김길동]", dto.getSupplierUserName())
                .replace("[010-2025-5202]", dto.getBuyerUserPhone())
                .replace("[010-5202-2025]", dto.getSupplierUserPhone())
                .replace("[(주)BizOnDam]", dto.getBuyerCompanyName())
                .replace("[(주)SKU]", dto.getSupplierCompanyName())
                .replace("[2025년 06월 17일]", formatDate(dto.getContractCreatedAt()))
                .replaceAll("(?s)<tbody>.*?</tbody>", "<tbody>" + tableRows + "</tbody>");
    }

    private String formatPrice(Number price) {
        return String.format("%,d", price);
    }

    private String formatDate(String dateStr) {
        if (dateStr == null) return "-";
        return dateStr.replace("-", "년 ").replaceFirst("년 (\\d+)", "년 $1월 ").replaceFirst("월 (\\d+)", "월 $1일");
    }

    private String formatDate(java.time.LocalDateTime date) {
        if (date == null) return "-";
        return String.format("%d년 %02d월 %02d일", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }
}
