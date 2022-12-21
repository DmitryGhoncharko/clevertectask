# clevertectask
#Используемые технологии:
1. Jakarta EE(Servlets)
2. JDBC
3. Postgresql
4. Apache Tomcat
5. Lombok
6. Junit
7. Sl4J
8. GSON
9. Mockito
10.Gradle
#Инструкция по запуску:
1.Java 17 или выше
2.Поднять Postgresql с пользователем dangeonmaster и паролем jmXzj3eV
3.Порт постгресс 5432 database называется task -- jdbc:postgresql://localhost:5432/task
4.Выдать пользователю dangeonmaster права на доступ к database task
5.Запустить градл скрипт в корне проекта и положить вар в апаче томкат вебаппс 10.0 или выше
6.Либо взять вар архив в корне проекта и расположить его в апаче томкат вебаппс версии 10.0 или выше
#Эндпоинты:
1. /check - возвращает чек со списком товаров в json формате, пример использования -- /check?product=1&count=2&discount=1(последний параметр опционально)
