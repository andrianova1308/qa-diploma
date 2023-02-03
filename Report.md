# Отчет по итогам автоматизированного тестирования

## Краткое описание

Реализовано автоматизированное тестирование веб-cервиса покупки тура, взаимодействующего с СУБД и API Банка согласно [плану тестирования](../main/Plan.md).

Проведены UI тесты при подключении к 2 БД. Общее количество тест-кейсов: **60**

---

## Итоги тестирования

### *При подключении к БД MySQL*

![Allure Report SQL 1](../main/screen/sql.png)

- Успешных кейсов 55% (38 кейсов)
- Неуспешных кейсов 45% (30 кейсов)

![Allure Report SQL 2](../main/screen/first1.png)
![Allure Report SQL 3](../main/screen/first2.png)
![Allure Report SQL 4](../main/screen/second1.png)
![Allure Report SQL 5](../main/screen/second2.png)

---

### *При подключении к БД PostgreSQL*
![Allure Report PostgreSQL](https://raw.githubusercontent.com/Romankurinov/Graduate-work/main/.github/images/img_5.png)

- Успешных кейсов 53% (36 кейсов)
- Неуспешных кейсов 47% (32 кейсов)

![Allure Report PostgreSQL 1](https://raw.githubusercontent.com/Romankurinov/Graduate-work/main/.github/images/img_6.png)
![Allure Report PostgreSQL 2](https://raw.githubusercontent.com/Romankurinov/Graduate-work/main/.github/images/img_8.png)
![Allure Report PostgreSQL 3](https://raw.githubusercontent.com/Romankurinov/Graduate-work/main/.github/images/img_9.png)
![Allure Report PostgreSQL 4](https://raw.githubusercontent.com/Romankurinov/Graduate-work/main/.github/images/img_10.png)

---
## Общие рекомендации

1. Исправить все найденные баги, полный список которых расположен в [Issues](https://github.com/Romankurinov/Graduate-work/issues)

4. Заменить ошибку "Неверный формат" при вводе недостаточного количества цифр в поле "Номер карты" на "Указано недостаточно цифр"/"Укажите точно как на карте"

6. Заменить ошибку "Неверный формат" при вводе недостаточного количества цифр в поле "CVC/CVV" "Поле должно состоять из 3 цифр"/"Укажите точно как на карте"

7. Сделать уведомление при вводе большого количества символов между именем владельца карты и фамилией - "Допустим только один пробел между именем и фамилией"

8. Сделать уведомление при вводе в поле "Владелец" большого количества символов - "Допустимо не более ** символов"

9. Исправить проблему с подключением к БД PostgreSQL, так как аналогичные тесты проходят при подключении к БД MySQL

10. Реализовать возможность оплаты картами не только из 16 цифр, т.к. существуют валидные карты, состоящие из 13-19 цифр

11. Сделать неактивной кнопку "Продолжить" в случае если все поля формы пустые или есть незаполненные и/или неправильно заполненные поля
