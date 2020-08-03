package com.marekhudyma.style.domain.command.util;

public interface Command<INPUT, RESULT> {

    RESULT execute(INPUT input);

}
