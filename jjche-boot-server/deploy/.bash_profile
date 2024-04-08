JAVA_OPTS="-Xms4096m -Xmx4096m -XX:NewSize=2250M -XX:MaxNewSize=2250M -XX:PermSize=256m -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError, -XX:+ExitOnOutOfMemoryError, -XX:HeapDumpPath=./ -Dfile.encoding=utf-8"
JAVA_WEB_SERVICE_OPTS="--spring.profiles.active=test"

#mysql
export JJCHE_DB_HOST=""
export JJCHE_DB_DATABASE=""
export JJCHE_DB_UNAME=""
export JJCHE_DB_PASSWORD=""
export JJCHE_DB_PORT="36010"

#redis
export JJCHE_REDIS_DB=""
export JJCHE_REDIS_HOST=""
export JJCHE_REDIS_PORT="16379"
export JJCHE_REDIS_PASSWORD=""

#sba
export JJCHE_SBA_PASSWORD=""

#user
export JJCHE_USER_PASSWORD_DEFAULT-VAL=""

#filter
JJCHE_FILTER_ENCRYPTION_FIELD_SECRET=""

#jwt
export JJCHE_SECURITY_JWT_BASE64-SECRET=""
