# 니돈내돈
### 2024-1 캡스톤디자인 객체지향프로젝트 수업 


![제목을 입력해주세요_-002 (2)](https://github.com/user-attachments/assets/c36d94ce-5814-4788-a8db-92a677eddb10)
java 언어로 프로젝트를 진행했습니다.

## 👩🏻‍💻 My experience
작년 2023-2 독일 교환학생으로 6개월동안 해외에 거주해보며 다른 친구들과 함께 가계부를 사용해야만 하는 상황이 많았습니다. 여러 번 여행을 가거나 같이 생활용품을 구매하는 등 또한 유로로 결제하거나 한화로 결제 하는 등 이러한 문제를 모두 포괄해서 해결해주는 어플이 없어서 직접 구글 스프레드 시트를 활용해 간이 가계부를 6개월동안 사용했습니다.

![image](https://github.com/jeongmin1016/mymoneyyourmoney_/assets/109460178/5faafde7-d804-4b45-898d-9d2ce893b3bf)

## 👩🏻‍💻 main process
다른 사람들과 함께 지출을 해야하는 상황에서 조금 더 효율적으로 모두가 동등한 지출을 할 수 있도록 하는 "니돈내돈" 어플리케이션 개발

- 현재 누가 얼마를 어디에 썼는지, 각각 지출한 정도에 따라 다음 결제는 누가하는 것이 적합한지 알려줌
- 여행뿐만 아니라 다양한 크고 작은 그룹활동에서 발생하는 비용에 대해 중간중간 서로의 지출 밸런스를 맞춰주는 '니돈내돈' 서비스
- 주요 기능 : 지출 밸런스 시각화, 실시간 환율 반영, 소비내역 작성 시 이미지 삽입 가능, 소비 내역 분배할 인원수 선택 가능

## 👩🏻‍💻 my role
### 💰 information architecture

![스크린샷 2024-04-29 오후 1 33 22](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/97f1c876-d9b6-4213-ad32-4b379e0b0cb9)

### 💰 Front-end
figma 기반으로 android studio를 이용해 java, xml 적용
#### page7 DetailAccountView
- [버튼] 수정하기
    
    ⇒ `InputView` 페이지로 돌아감
    

`InputView` 페이지 입력값 출력

- 사진
- 상세 표시
    - 항목
        - 카테고리
    - 날짜
    - 금액
- For whom 체크 박스 받아오기
    - Yes
        - 친구 리스트 출력
    - No
        - 친구 리스트 미출력

#### page 8&9 SettingAccount
- [버튼] 가계부 생성
    
    ⇒ Form 입력 가능
    

- 가계부 이름
- 일정
    - [달력]시작일, 종료일 모두 선택
- 통화
    - [리스트] 통화 종류 선택
        - [체크박스]현재 통화 적용 체크
            - API 가져옴
        - [체크박스]현재 통화 적용 미체크
            - [입력]사용자 직접 입력
- 참여자
    - (기본값) 생성자 이름
    - [추가 버튼] 입력시, 친구 추가

## 최종 화면
| ![Group 1](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/2ad53cd1-c630-47a9-b069-4e32d671e5e8) | ![Group 2](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/2dbc51d9-854b-47fa-a6ba-3e8e57517278) | ![Group 3](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/6fd90980-0e16-4cb1-8253-cbe607d5e63e) |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![Group 5](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/48c1a957-8aff-45d6-a784-2b83687f3e78) | ![Group 9](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/07eb3fc3-c10e-45da-b7ba-c84a274d910e) | ![Group 7](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/8a8e2342-44d3-4868-8074-b0ecf1fed5f2) |
| ![Group 10](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/881b028f-e3c0-4598-b74f-6a800e70da1b) | ![Group 8](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/d8c1ef66-0a43-4ae5-bf70-2cbcb4f410b3) | ![Group 4](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/6714b559-4173-4e7d-92dd-e5157ab89f00) |

## 사용 기술 스택
![Multiple choice](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/5ef31882-a0ae-45f0-ba96-45f6791e92bd)

## Data Flow
![Account Book project](https://github.com/SMWU-NaesoneulJAVA/frontend/assets/89966409/e2502800-43f1-4cb4-8a13-77c64c303aec)
