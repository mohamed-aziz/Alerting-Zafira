package utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class Tasks {
    @NonNull
    Runnable task;
    @NonNull
    int period;
    @NonNull
    TimeUnit unit;
}
