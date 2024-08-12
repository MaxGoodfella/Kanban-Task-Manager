### О проекте

Kanban-Task-Manager - монолит, использующий подход Kanban к управлдению задачами. Основа приложения - разделение на 3 типа: задачи, эпики, подзадачи. 
Также присутствует статус каждой из задач - in_progress, new, done. 

Стек технологий: Java, JUnit, Insomnia. 

Набор запросов в Insomnia: https://disk.yandex.com/d/ZBgfrJz0E9SzvQ

### Функциональность
- хранение данных в оперативной памяти, загрузка и сохранение данных;
- создание, получение, удаление, обновление, управление статусами всех типов задач
- получение истории просмотров задач.

### Запуск приложения

Приложение поднимается c помощью фреймворка Spring, при запуске автоматически создаётся файл text.txt.

Порядок запуска приложения:
1. *Клонирование репозитория*
```
git clone https://github.com/MaxGoodfella/Kanban-Task-Manager.git
cd Kanban-Task-Manager
```

2. *Запуск приложения через Main.java*

3. *Запуск коллекции запросов*

Коллекция insomnia-запросов доступна по сслылке выше.
