package org.example.weneedbe.domain.file.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.weneedbe.domain.article.domain.Article;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public File(String fileUrl, Article article) {
        this.fileUrl = fileUrl;
        this.article = article;
    }
}
