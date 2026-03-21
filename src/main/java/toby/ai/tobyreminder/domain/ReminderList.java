package toby.ai.tobyreminder.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ReminderList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color = "#007AFF";

    @Column(nullable = false)
    private String icon = "list.bullet";

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public ReminderList(String name, String color, String icon, Integer displayOrder) {
        this.name = name;
        this.color = color != null ? color : "#007AFF";
        this.icon = icon != null ? icon : "list.bullet";
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public void update(String name, String color, String icon) {
        this.name = name;
        this.color = color != null ? color : this.color;
        this.icon = icon != null ? icon : this.icon;
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
