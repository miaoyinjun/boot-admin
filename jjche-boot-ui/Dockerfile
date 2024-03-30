FROM nginx:1.19.6

ARG TZ="Asia/Shanghai"
ENV TZ ${TZ}
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && rm -rf /var/cache/apk/*

COPY ./dist /data

RUN rm /etc/nginx/conf.d/default.conf

ADD ui.conf /etc/nginx/conf.d/

RUN /bin/bash -c 'echo init ok'
