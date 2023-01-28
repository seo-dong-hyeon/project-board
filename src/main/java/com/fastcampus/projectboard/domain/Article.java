package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL auto increment
    private Long id;

    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문

    @Setter private String hashtag; // 해시태그

    // 순환참조 방지
    // ToString -> 모든 원소를 찍기 위해 조회 -> ArticleComment -> ToString -> Article 원소 -> ...
    // 두 군데 중 한 군데를 끊어줘야
    @ToString.Exclude
    @OrderBy("id")
    // article table로부터
    // 실제론 운영 및 백업 및 성능 목적으로 FK를 안걸고 cascase를 안하는 경우도 많음
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    // set -> article에 연동된 comment -> 중복 허용 x -> 모아서 컬렉션으로
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    // 컬렉션에서 사용을 위해(게시글 리스트)
    // 리스트에 넣기, 리스트에서 중복 요소 제거, 정렬을 위한 비교 필요
    // id가 같으면 같은 entity(객체)로 판단
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // pattern variable -> pattern matching
        if (!(o instanceof Article article)) return false;
        // 데이터베이스에 insert하지 않았을 때(영속화x) -> id = null
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
