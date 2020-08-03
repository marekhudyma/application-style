package com.marekhudyma.style.domain.query.util;


public interface Query<INPUT, RESULT> {

    RESULT execute(INPUT input);

}
