# Отчет по итогам автоматизированного тестирования

## Краткое описание

Реализовано автоматизированное тестирование веб-cервиса покупки тура, взаимодействующего с СУБД и API Банка согласно [плану тестирования](../main/Plan.md).

Проведены UI тесты при подключении к 2 БД. Общее количество тест-кейсов: **60**

---

## Итоги тестирования

### *При подключении к БД MySQL*

![Allure Report SQL 1](https://github.com/andrianova1308/qa-diploma/blob/main/screen/img.png)

- Успешных кейсов 60% (46 кейсов)
- Неуспешных кейсов 40% (24 кейсов)

![Allure Report SQL 2](https://github.com/andrianova1308/qa-diploma/blob/main/screen/first1.PNG)
![Allure Report SQL 3](https://github.com/andrianova1308/qa-diploma/blob/main/screen/first2.PNG)
![Allure Report SQL 4](https://github.com/andrianova1308/qa-diploma/blob/main/screen/second1.PNG)
![Allure Report SQL 5](https://github.com/andrianova1308/qa-diploma/blob/main/screen/second1.PNG)

---

### *При подключении к БД PostgreSQL*
![Allure Report PostgreSQL](https://github.com/andrianova1308/qa-diploma/blob/main/screen/postgresql.PNG)

- Успешных кейсов 60% (46 кейсов)
- Неуспешных кейсов 40% (24 кейсов)

![Allure Report PostgreSQL 1](https://github.com/andrianova1308/qa-diploma/blob/main/screen/3th1.PNG)
![Allure Report PostgreSQL 2](https://github.com/andrianova1308/qa-diploma/blob/main/screen/3th2.PNG)
![Allure Report PostgreSQL 3](https://github.com/andrianova1308/qa-diploma/blob/main/screen/4th1.PNG)
![Allure Report PostgreSQL 4](https://github.com/andrianova1308/qa-diploma/blob/main/screen/4th2.PNG)

---
## Общие рекомендации

1. Исправить все найденные баги, полный список которых расположен в [Issues](https://github.com/andrianova1308/qa-diploma/issues)

4. Заменить ошибку "Неверный формат" при вводе недостаточного количества цифр в поле "Номер карты" на "Указано недостаточно цифр"/"Укажите точно как на карте"

6. Заменить ошибку "Неверный формат" при вводе недостаточного количества цифр в поле "CVC/CVV" "Поле должно состоять из 3 цифр"/"Укажите точно как на карте"

7. Сделать уведомление при вводе большого количества символов между именем владельца карты и фамилией - "Допустим только один пробел между именем и фамилией"

8. Сделать уведомление при вводе в поле "Владелец" большого количества символов - "Допустимо не более ** символов"

9. Исправить проблему с подключением к БД PostgreSQL, так как аналогичные тесты проходят при подключении к БД MySQL

10. Реализовать возможность оплаты картами не только из 16 цифр, т.к. существуют валидные карты, состоящие из 13-19 цифр

11. Сделать неактивной кнопку "Продолжить" в случае если все поля формы пустые или есть незаполненные и/или неправильно заполненные поля
