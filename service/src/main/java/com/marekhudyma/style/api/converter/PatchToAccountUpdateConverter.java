package com.marekhudyma.style.api.converter;

import com.marekhudyma.style.domain.command.UpdateAccountCommand;
import com.marekhudyma.style.domain.model.AccountId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatchToAccountUpdateConverter {

    public UpdateAccountCommand.AccountUpdate convert(UUID accountId, int scoring, String version) {
        return new UpdateAccountCommand.AccountUpdate() {
            @Override
            public AccountId getId() {
                return AccountId.from(accountId);
            }

            @Override
            public int getScoring() {
                return scoring;
            }

            @Override
            public String getVersion() {
                return version;
            }
        };
    }

}
