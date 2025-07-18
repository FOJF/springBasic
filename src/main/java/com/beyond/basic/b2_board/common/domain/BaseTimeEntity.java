package com.beyond.basic.b2_board.common.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

//기본적으로 Entity는 상속이 불가능. MappedSuperClass 어노테이션 사용시 상속 가능
@MappedSuperclass
@Getter
public class BaseTimeEntity {
//    entity 마다 사용되는 컬럼이기 때문에 따로 분리
//    컬럼명에 캐멀케이스 사용시, db에는 created_time과 같이 언더스코어로 자동 변경해줌

    //    아래 두 어노테이션은 실제로는 작동하지만 db에서는 알 수 없음(describe해도 정보가 없음)
    @CreationTimestamp // db의 current timestamp와 같은 기능
    private LocalDateTime createdTime;
    @UpdateTimestamp // entity의 값이 변경되면 자동으로 시간 저장
    private LocalDateTime updatedTime;
}
