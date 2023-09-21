# java-kanban
Repository for homework project.

Привет, Артём!  **_Спасибо за проверку_**!

Комментарий: 
Плюсом к твоим комментариям перенёс логику работы loadFromFile() из класса FileBackedTaskManager в метод fromString() в
классе CSVManager. Как мне кажется довольно логично, потому что до этого он (fromString) вообще не использовался и я 
не понимал, как его перенести, но теперь смог) Ну и раз логика toString() реализована в CSVManager, то и fromString()
лучше расположить там же. 