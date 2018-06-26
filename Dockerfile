FROM openjdk:8-jre-alpine
COPY ./backend/build/libs/todoapp.jar /root/
RUN mkdir /root/frontend
COPY ./frontend/build/bundle/frontend.bundle.js /root/frontend/
WORKDIR /root
CMD [                                   \
    "java", "-server",                  \
    "-XX:+UnlockExperimentalVMOptions", \
    "-XX:+UseCGroupMemoryLimitForHeap", \
    "-XX:InitialRAMFraction=2",         \
    "-XX:MinRAMFraction=2",             \
    "-XX:MaxRAMFraction=2",             \
    "-XX:+UseG1GC",                     \
    "-XX:MaxGCPauseMillis=100",         \
    "-XX:+UseStringDeduplication",      \
    "-jar",                             \
    "todoapp.jar"                       \
    ]