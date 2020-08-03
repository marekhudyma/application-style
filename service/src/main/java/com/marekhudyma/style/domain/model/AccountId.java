package com.marekhudyma.style.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccountId implements Serializable, Comparable<AccountId> {

    private UUID value;

    public static AccountId from(int value) {
        return new AccountId(new UUID(0, value));
    }

    public static AccountId from(UUID value) {
        return new AccountId(value);
    }

    @Override
    public int compareTo(AccountId accountId) {
        return this.value.compareTo(accountId.getValue());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static class DbConverter implements AttributeConverter<AccountId, UUID> {

        @Override
        public UUID convertToDatabaseColumn(AccountId id) {
            return id.value;
        }

        @Override
        public AccountId convertToEntityAttribute(UUID value) {
            return new AccountId(value);
        }
    }
}
