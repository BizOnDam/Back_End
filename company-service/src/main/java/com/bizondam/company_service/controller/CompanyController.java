package com.bizondam.company_service.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.company_service.dto.*;
import com.bizondam.company_service.entity.CompanyUser;
import com.bizondam.company_service.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "기업 등록 API", description = "기업 정보 등록 및 조회 관련 API")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @Operation(summary = "기업 등록", description = "기업의 최초 가입자 회원가입 시 기업을 등록해주는 API")
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<CompanyResponse>> registerCompany(
        @Valid @RequestBody CompanyRequest dto) {
        CompanyResponse response = companyService.createCompany(dto);
        return ResponseEntity
            .ok(BaseResponse.success("기업 등록에 성공했습니다.", response));
    }

    @Operation(summary = "사업자 등록 번호 검증", description = "사업자 등록 번호 검증 및 기업 가입 여부 확인 API")
    @PostMapping("/validate")
    public ResponseEntity<BaseResponse<CompanyValidateResultResponse>> validateBusiness(
        @RequestBody CompanyValidationRequest dto) {
        try {
            CompanyRequest companyRequest = mapToCompanyRequest(dto);
            CompanyValidateResultResponse response = companyService.validateBusiness(companyRequest);

            if (!response.isValidBusinessNumber()) {
                return ResponseEntity
                    .badRequest()
                    .body(BaseResponse.fail("유효하지 않은 사업자 등록번호입니다.", null));
            }
            return ResponseEntity.ok(
                BaseResponse.success("사업자 등록번호 검증에 성공했습니다.", response)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(BaseResponse.fail("유효성 검사 실패: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error(500, "서버 오류가 발생했습니다."));
        }
    }

    private CompanyRequest mapToCompanyRequest(CompanyValidationRequest validationRequest) {
        if (validationRequest == null) {
            throw new IllegalArgumentException("요청 데이터가 null입니다.");
        }
        return CompanyRequest.builder()
            .businessNumber(validationRequest.getB_no())
            .startDate(validationRequest.getStart_dt())
            .ceoNameKr(validationRequest.getP_nm())
            .companyNameKr(validationRequest.getB_nm())
            .build();
    }

    @Operation(summary = "기업 정보 조회", description = "companyId를 기반으로 기업 정보를 조회")
    @GetMapping("/{companyId}")
    public ResponseEntity<BaseResponse<CompanyResponse>> getCompanyById(@PathVariable Long companyId) {
        CompanyResponse response = companyService.getCompanyInfo(companyId);
        return ResponseEntity.ok(BaseResponse.success("기업 조회에 성공했습니다.", response));
    }

    // CEO 권한 API들
    @Operation(summary = "직원 목록", description = "회사의 전체 직원 목록")
    @GetMapping("/staff/list")
    public ResponseEntity<BaseResponse<List<CompanyUser>>> getUsersInCompany(
        @RequestParam Long companyId,
        @RequestHeader("X-User-Id") Long userId,
        @RequestHeader("X-User-Role") String userRole
    ) {
        List<CompanyUser> users = companyService.getUsersInCompany(userRole, companyId);
        return ResponseEntity.ok(BaseResponse.success("회사 직원 목록 조회", users));
    }

    @Operation(summary = "직원 상세 정보", description = "특정 직원의 상세 정보")
    @GetMapping("/staff-info/{targetUserId}")
    public ResponseEntity<BaseResponse<CompanyUser>> getUserDetail(
        @PathVariable Long targetUserId,
        @RequestParam Long companyId,
        @RequestHeader("X-User-Id") Long userId,
        @RequestHeader("X-User-Role") String userRole
    ) {
        CompanyUser user = companyService.getUserDetail(userRole, companyId, targetUserId);
        return ResponseEntity.ok(BaseResponse.success("직원 상세 정보 조회", user));
    }

    @Operation(summary = "직원 삭제", description = "CEO 관리")
    @PatchMapping("/staff/delete/{targetUserId}")
    public ResponseEntity<BaseResponse<String>> deleteStaff(
        @RequestParam Long companyId,
        @RequestHeader("X-User-Id") Long userId,
        @RequestHeader("X-User-Role") String userRole,
        @PathVariable Long targetUserId
    ) {
        companyService.deleteStaff(userId, userRole, companyId, targetUserId);
        return ResponseEntity.ok(BaseResponse.success("직원 삭제 완료", null));
    }

    @Operation(summary = "직원 정보 수정", description = "CEO가 직원의 부서, 직책, 역할 설명을 수정")
    @PatchMapping("/staff/update/{targetUserId}")
    public ResponseEntity<BaseResponse<String>> updateStaff(
        @RequestParam Long companyId,
        @RequestHeader("X-User-Id") Long userId,
        @RequestHeader("X-User-Role") String userRole,
        @PathVariable Long targetUserId,
        @RequestBody StaffUpdateRequest request
    ) {
        companyService.updateStaff(userId, userRole, companyId, targetUserId, request);
        return ResponseEntity.ok(BaseResponse.success("직원 정보가 수정되었습니다.", null));
    }

    @Operation(summary = "CEO 변경", description = "현재 CEO가 다른 직원을 CEO로 지정, 자신은 STAFF로 변경")
    @PatchMapping("/staff/transfer-ceo/{targetUserId}")
    public ResponseEntity<BaseResponse<String>> transferCeoRole(
        @RequestParam Long companyId,
        @RequestHeader("X-User-Id") Long userId,
        @RequestHeader("X-User-Role") String userRole,
        @PathVariable Long targetUserId
    ) {
        companyService.transferCeoRole(userId, userRole, companyId, targetUserId);
        return ResponseEntity.ok(BaseResponse.success("CEO 권한이 성공적으로 이전되었습니다.", null));
    }
}