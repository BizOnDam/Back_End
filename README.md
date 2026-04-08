# 🏢 BizOnDam

**중고 B2B 장비 거래를 안전하고 투명하게 연결하는 MSA 기반 플랫폼**

- ⏳ **진행 기간**: 2025.04.22 ~ 2025.06.27  
- 🔗 **Organization**: [BizOnDam](https://github.com/BizOnDam)

<br>

## 🧾 프로젝트 개요
- **정의**: 수요·공급 매칭을 자동화하고 객관적인 의사결정 지원을 제공하는 플랫폼  
- **필요성**: 방대한 조달 데이터를 효율적으로 처리하여 정확한 매칭과 비용 절감  
- **특징/기대효과**: 시간 절감, 효율적 매칭, 데이터 기반 추천  

<br>

## 👨‍👩‍👧‍👦 구성원
| Name | Role | GitHub |
|------|------|--------|
| 고현서 | Back-End / MSA 인프라 | [@hyunseoko](https://github.com/hyunseoko) |
| 윤가영 | Front-End / Batch, Quartz | [@Yung-Ga](https://github.com/Yung-Ga) |

<br>

## ⚙️ 시스템 개요
- **Config Server**: 외부 설정 관리 (GitHub Repo 연동)  
- **Eureka Server**: 마이크로서비스 서비스 디스커버리  
- **API Gateway**: 요청 라우팅 및 필터링  
- **Spring Batch + Quartz**: 조달 데이터 수집/배치  
- **OpenAI 기반 추천 API**: 공급업체 추천 리스트 생성  
- **OpenFeign + CircuitBreaker**: 마이크로서비스 간 호출 단순화 및 장애 대응  

<br>

## 🛠 서비스 구성 요약
1. **회원 가입/기업 인증** → 국세청 사업자 등록 검증 API  
2. **견적 요청** → estimate 테이블에 데이터 저장  
3. **공급기업 추천** → 내부 DB + OpenAI GPT 기반 추천 리스트 제공  
4. **계약서 관리** → Google Cloud Storage 기반 저장/다운로드  
5. **배치 처리** → Quartz 스케줄러 + Batch로 최신 상태 반영
   
<br>

![image07](https://github.com/user-attachments/assets/fc5775f1-2256-4179-95f7-5d3b061808e7)
![image01](https://github.com/user-attachments/assets/ce02d674-ab83-428c-9014-a36e20769477)
![image02](https://github.com/user-attachments/assets/65431296-7f5a-41f6-9500-c0bc32329516)
![image03](https://github.com/user-attachments/assets/452429d2-a818-4cb4-a0b8-026dc0ae0ae5)
![image04](https://github.com/user-attachments/assets/f7ff381f-98e1-4db3-8f32-188eb77a537f)
![image05](https://github.com/user-attachments/assets/0af26269-93da-422c-8307-144f11e5c107)
![image06](https://github.com/user-attachments/assets/b700002b-4393-4a36-9577-5f7676f32ec4)
