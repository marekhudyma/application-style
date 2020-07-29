package com.marekhudyma.testcontainers.domain.command.util;

import com.marekhudyma.testcontainers.domain.command.util.Result;

public interface Command<INPUT, RESULT extends Result> {

  RESULT execute(INPUT input);

}
