package com.marekhudyma.testcontainers.domain.command.util;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<ERROR extends Enum<ERROR>, TYPE> {

  private final ERROR error;
  private final TYPE value;

  public static <ERROR extends Enum<ERROR>, TYPE> Result<ERROR, TYPE> fail(ERROR error) {
    return new Result<>(requireNonNull(error), null);
  }

  public static <ERROR extends Enum<ERROR>, TYPE> Result<ERROR, TYPE> ok(TYPE value) {
    return new Result<>(null, requireNonNull(value));
  }

  public boolean isError() {
    return error != null;
  }

  public Optional<ERROR> getError() {
    return Optional.ofNullable(error);
  }

  public Optional<TYPE> getValue() {
    return Optional.ofNullable(value);
  }

  public <OUTPUT> OUTPUT map(Function<ERROR, OUTPUT> mapError, Function<TYPE, OUTPUT> mapValue) {
    return isError() ? mapError.apply(error) : mapValue.apply(value);
  }
}
