# 요구사항

## 상품 목록 및 행사 목록 파일 읽기

### 필수 요구사항

- 불러올 파일의 내용형식을 유지해야 한다. (값은 수정할 수 있음)
- 상품 목록 파일은 상품의 이름, 가격, 재고, 프로모션 정보를 가진다.
- 행사 목록 파일은 행사의 이름, 구매 개수, 증정 개수, 시작일, 종료일을 가진다.
- 파일의 첫번째 줄은 각 데이터의 카테고리를 나타낸다.
- 파일의 두번째 줄부터 데이터가 시작된다.
- 파일의 데이터는 쉼표로 구분된다. (csv)

### 추가된 요구사항

- 파일이 존재하지 않을 경우, 프로그램은 종료된다.
- 파일의 내용이 잘못되었을 경우, 에러를 발생하고, 프로그램은 종료된다.
    - 데이터의 형식이 잘못되었을 경우
    - 데이터의 타입이 잘못되었을 경우
    - 데이터가 누락 되었을 경우

## 재고 관리

### 필수 요구사항

- 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.
    - 결제가 여러 곳에서 이루어질 수 있음을 고려해야하나? (동시성 문제)

### 추가된 요구사항

- 상품은 이름, 가격, 재고, 프로모션 정보를 가진다.
- 상품의 재고는 일반 재고와 프로모션 재고로 나뉜다.
- 상품의 재고가 없으면 구매할 수 없다.
- 상품의 재고는 0 이상이어야 한다.
- 상품의 가격은 0보다 커야한다.
- 상품의 이름은 중복될 수 없다.
    - 상품 목록 파일을 읽어드리는 과정에서 이름이 같다면 같은 상품으로 인식해야 한다.
    - 예를 들어 상품 목록 파일에 일반 재고 콜라와 프로모션 재고 콜라가 있다면, 같은 상품으로 인식한다.
    - 단, 이름이 같아도 같은 타입의 상품은 여러개 존재할 수 없다.
- 상품의 이름은 1자 이상, 50자 이하이어야 한다.
- 상품의 이름은 대소문자를 구분하지 않는다.
- 상품의 가격은 1원 이상이어야 한다.
- 상품의 가격 및 재고는 integer 범위 내에 있어야 한다.

## 2. 프로모션

### 필수 요구사항

- 프로모션은 이름, 구매 개수, 증정 개수, 시작일, 종료일을 가진다.
- 오늘 날짜가 프로모션의 시작일과 종료일 사이에 있으면 프로모션을 적용한다.
- 프로모션은 N개 구매시 1개 무료 증정의 형태로 진행된다.
    - N개 구매시 1개 무료 증정의 '형태' 이기 떄문에, 무료증정은 1개 이상이 될 수도 있다.
- 프로모션은 여러 상품에 적용될 수 있다. (1:N)
- 프로모션 해택은 프로모션 재고 내에서만 적용할 수 있다.
- 한 상품에 여러 프로모션이 적용될 수 없다. (1:1)
- 프로모션 기간 중이면 프로모션 재고를 우선으로 차감한다. (프로모션 재고가 없으면 일반 재고에서 차감)
    - e.g. 프로모션 재고가 2개이고, 2+1 프로모션이라면 무료증정을 할 수 없기 때문에 일반 재고에서 차감한다.
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.

### 추가된 요구사항

- 프로모션 기간이 아니더라도 프로모션 부터 처리한다.
- 프로모션은 이름이 중복 될 수 없다.
    - 프로모션을 읽어드리는 파일에서 중복된 이름의 프로모션이 있을 경우, 상품 목록 파일에 있는 프로모션을 구분하지 못한다.
    - 때문에 프로모션의 이름은 중복될 수 없다.
- 프로모션의 이름은 대소문자를 구분하지 않는다.
- 프로모션의 이름은 1자 이상, 50자 이하이어야 한다.
- 프로모션의 시작일은 종료일보다 이전이어야 한다.
- 구매 개수는 1 이상이어야 한다.
- 증정 개수는 1 이상이어야 한다.

## 멤버십 할인

### 필수 요구사항

- 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
- 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- 멤버십 할인의 최대 한도는 8,000원이다.

### 추가된 요구사항

- 멤버십 회원은 추후 여러가지로 확장될 수 있다.
- 할인된 금액은 소수점 이하를 버림하여 적용한다.
- 할인률은 0 초과 100 미만의 정수로 표현한다.
    - e.g. 30% 할인은 30으로 표현한다.

## 영수증 출력

### 필수 요구사항

- 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
- 영수증 항목은 아래와 같다.
    - 구매 상품 내역: 구매한 상품명, 수량, 가격
    - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - 금액 정보
        - 총구매액: 구매한 상품의 총 수량과 총 금액
        - 행사할인: 프로모션에 의해 할인된 금액
        - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
        - 내실돈: 최종 결제 금액
- 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.

### 추가된 요구사항

- 구성 요소를 보기 좋게 정렬
    - 상품명이 길어지면 뒤쪽 부분을 생략하고 '...'으로 표시한다.
- 생략한 이름 때문에 구분이 어려울 수 있기 때문에 ID를 표시한다.
- 상품명은 왼쪽정렬, 숫자(금액, 수량)는 오른쪽 정렬한다.
- 금액은 천단위 구분자를 표시한다.
- 숫자는 줄바꿈과 생략 없이 한줄에 출력한다.

## 입출력 요구사항

- 구매할 상품과 수량을 입력 받는다.
    - 상품명과 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
    - e.g. `[콜라-2, 사이다-1]`
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
    - Y: '증정 받을 수 있는 상품'을 추가한다.
    - N: '증정 받을 수 있는 상품'을 추가하지 않는다.
    - 증정을 받을 수 있는 상품이다. 떄문에 구매개수는 충족하였으나 증정 상품을 가져오지 않을 때를 말한다.
        - e.g. 2+1 상품인데 고객이 1개만 가져온 경우는 제외.
    - e.g. 1+1 상품인데 고객이 1개만 가져왔다면, 1개를 무료로 받을 수 있음을 안내한다.
    - e.g. 2+1 상품인데 고객이 2개만 가져왔다면, 1개를 무료로 받을 수 없음을 안내한다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
    - Y: 일부 수량에 대해 정가로 결제한다.
    - N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
    - e.g. 1+1 상품인데 고객이 4개를 가져왔고, 프로모션 상품 재고가 3개라면, 2개를 정가로 결제할지 여부를 묻는다.
    - e.g. 1+1 상품인데 고객이 4개를 가져왔고, 프로모션 상품 재고가 2개라면, 2개를 정가로 결제할지 여부를 묻는다.
    - e.g. 1+1 상품인데 고객이 2개를 가져왔고, 프로모션 상품 재고가 1개라면, 2개를 정가로 결제할지 여부를 묻는다.
- 멤버십 할인 적용 여부를 입력 받는다.
    - Y: 멤버십 할인을 적용한다.
    - N: 멤버십 할인을 적용하지 않는다.
- 추가 구매 여부를 입력 받는다.
    - Y: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
    - N: 구매를 종료한다.

# 기능 구현 목록

- [ ] 상품 목록 파일 및 프로모션 목록 파일 읽기 기능.
- [ ] 상품 entity
- [ ] 상품을 관리하는 재고관리 entity
- [ ] 프로모션을 관리하는 entity
- [ ] 멤버십 할인 entity
- [ ] 영수증 entity