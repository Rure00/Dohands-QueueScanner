# Barcode Scan Offline Sync Prototype (Android)

물류센터/현장 환경에서 네트워크가 불안정해도 작업(바코드 스캔)이 끊기지 않도록, 스캔 이벤트를 로컬에 안전하게 큐잉하고 동기화(Sync)로 원격 전송 결과를 반영하는 프로토타입입니다.

**핵심 목표: “바코드 입력 → 로컬 저장 → 동기화 → 상태 전이(PENDING/SENT/FAILED)” 흐름을 짧고 명확하게 증명**

------

## Tech Stack

- Kotlin
- Jetpack Compose (UI)
- CameraX (Camera)
- ML Kit Barcode Scanning
- Room (Local DB)
- Coroutines / Flow
- Hilt

## Architecture

<img width="436" height="248" alt="image" src="https://github.com/user-attachments/assets/fda077f8-7ca5-49af-aaf0-c68b9659ccb0" />


------

## Screens

<img width="682" height="1024" alt="image" src="https://github.com/user-attachments/assets/7482334a-8329-4c45-8620-cd5cfbe1ae2a" />



#### 1) ScanScreen

- CameraX 기반 카메라 프리뷰

- ML Kit Barcode Scanning으로 rawBarcode 획득

- rawBarcode 기반 Mock Job 생성 후 로컬 저장

#### 2) JobListScreen (Queue)

- 로컬에 쌓인 작업 목록 표시

- 상태 요약(PENDING/SENT/FAILED) 및 SYNC NOW 제공

- 실패 항목은 errorText 표시

#### 3) JobDetailScreen

- 선택된 Job의 상세 정보 표시

- Event ID / Retry Count / Barcode / Error 확인

- Event ID 복사(COPY EVENT ID)


### Flow 1

![first](https://github.com/user-attachments/assets/fa7b1199-3e41-4345-bfb2-02f20b65c11e)



1. 런타임 권한 요청: 
2. 바코드 스캔
3. 온라인 상태에서 스캔 시 바로 Remote 서버에 SEND 됨.


###  Flow2

![two](https://github.com/user-attachments/assets/1f83cc61-3b45-4e17-a9ad-89a6350934b4)


1. 여러 바코드를 한 번에 인식
2. 전송 성공 시 SENT
3. 실패 시 FAILED로 상태 반영 및 3회 재시도
4. Job Detail에서 클립보드에 Id 복사


------

## Data Model

```kotlin
data class Job(
    val id: String,
    val barcode: String,
    val time: LocalDateTime,
    val status: JobStatus,
    val errorText: String? = null,
    val retryCount: Int = 0
)
```

Status
- PENDING: 로컬 저장 완료, 전송 대기
- SENT: 전송 성공
- FAILED: 전송 실패(에러/재시도 카운트 기록)

```kotlin
sealed interface RemoteResult<out T> {
    data class Success<T>(val data: T) : RemoteResult<T>
    data class HttpError(val code: Int, val body: String?) : RemoteResult<Nothing>
    data class Offline(val e: IOException) : RemoteResult<Nothing>
    data class Unknown(val t: Throwable) : RemoteResult<Nothing>
}
```

물류센터/현장 환경에서의 다양한 네트워크 상태를 처리하기 위해 고안했습니다.

Success(성공), HttpError(서버에러), Offline(네트워크 끊김), Unknown(기타)를 정의하여 ViewModel에서 사용자에게 작업의 결과를 보여줍니다.

JobViewmodel.kt

```kotlin
when (remoteResult) {
    is RemoteResult.Success -> {
        _uiResult.value = UiResult.Idle
    }
    is RemoteResult.HttpError -> {
        _uiResult.value = UiResult.Fail(remoteResult.body ?: "알 수 없는 이유로 실패하였습니다.")
    }
    is RemoteResult.Offline -> {
        _uiResult.value = UiResult.Fail("네트워크를 확인해주세요.")
    }
    is RemoteResult.Unknown -> {
        _uiResult.value = UiResult.Fail(remoteResult.t.message ?: "알 수 없는 이유로 실패하였습니다.")
    }
}
```

Screen

```kotlin
LaunchedEffect(uiResult) {
    when (uiResult) {
        is UiResult.Fail -> {
          Log.i(JobViewModel.TAG, "Fail: ${(uiResult as UiResult.Fail).msg}")
          Toast.makeText(appContext, "실패했습니다.", Toast.LENGTH_SHORT).show()
        }
        else -> {  }
    }
}
```



------

#### Notes
>본 프로젝트는 “바코드 → 실제 물류 도메인 매핑(상품 조회, 주문 조회 등)”을 포함하지 않습니다.
이는 WMS/OMS 조회 및 프로세스 규칙이 필요한 별도 도메인 영역이며, 본 프로토타입은 오프라인 큐잉/동기화/정합성 흐름 검증에 집중했습니다.


