package com.marekhudyma.style.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.Instant;

@Entity
@Table(name = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
public class Account {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private AccountId id;

    @Generated(value = GenerationTime.INSERT)
    private Instant createdAt;

    @Generated(value = GenerationTime.ALWAYS)
    private Instant updatedAt;

    private String name;

    private int scoring;

    @Version
    private Integer version;

}
