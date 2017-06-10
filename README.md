# web_socket_chat_jee7
Нашёл в сети (http://www.hascode.com/2013/08/creating-a-chat-application-using-java-ee-7-websockets-and-glassfish-4/) пример
проекта web_socket-а, но он не заработал с 9-м tomcat-ом. Пришлось допилить. В ориганал добавлена коллекция
Map<Session, String> msg_sessions, в которой хранятся все подключившиеся сессии.
Проект собран и запускается в IntelliJ Idea. Чат открывается по адресу: http://localhost:8080/
