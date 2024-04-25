FROM fabric8/java-alpine-openjdk8-jre:1.9.0

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

RUN apk add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

ADD jjche-cloud-bpm-biz/target/jjche-cloud-bpm.jar /deployments
