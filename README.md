# java-explore-with-me
Дипломный проект
https://github.com/Mobsman/java-explore-with-me/pull/1

Приложение дает возможность делиться информацией о событиях и помогать найти компанию для участия в них

Приложение представляет собой микросервис. Создан он как многомодульное Java приложение 
благодаря возможностям maven делать многомодульные проекты. В проекте используется spring boot 2.7.2 и Java 11

Для запуска приложения понадобится: 
-Docker
-Java 11 
-Maven

Что бы запустить приложение нужно :
 1) создать пакеты jar при помощи команд - `mvn clean package` 
 2) в корне проекта выполнить команду - `docker compose up`

Настроить проект можно  в файле - docker-compose.yml 
который находится в корне проекта
По-умолчанию приложение запуститься на порту 8080

Спецификации API:
- [основного сервиса](ewm-main-service-spec.json)
- [сервиса статистики](ewm-stats-service-spec.json)

API комментариев:
1. PATCH /admin/comments/{commentId} - отредактировать комментарий пользователя;
2. DELETE /admin/comments/{commentId} - удалить комментарий пользователя;
3. POST users/{userId}/comments/events/{eventId} создать комментарий 
4. PATH users/{userId}/comments/events/{eventId} отредактирвоать комментарий
5. PATH users/{userId}/comments/{commentId}/events/{eventId} удалить комментарий

