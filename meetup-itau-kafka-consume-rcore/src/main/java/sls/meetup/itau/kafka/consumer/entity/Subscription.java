package sls.meetup.itau.kafka.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "subscription")
public class Subscription implements SignatureDTO {

    private transient String notificationType;

    @Id
    @Column(name="id")
    private String subscription;

    @Column(name="status_id")
    private Long statusId;

    @Column(name="created_at", columnDefinition = "DATE")
    private LocalDate createdAt;

    @Column(name="updated_at", columnDefinition = "DATE")
    private LocalDate updatedAt;

    public Subscription withUpdateAt(LocalDate updatedAt){
        this.updatedAt = updatedAt;
        return this;
    }

    public Subscription withStatusId(Long statusId){
        this.statusId = statusId;
        return this;
    }
}
