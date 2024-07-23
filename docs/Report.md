# Отчётные документы по итогам тестирования

## Краткое описание
Провел анализ системы оплаты туров для оплаты в кредит и нет. Покрыл сценариями систему [Plan.md](Plan.md) и на найденные
ошибки завел баги:
- Некорректно в базе данных отбиваются платежи https://github.com/dadosha/Java_Auto_Course/issues/1
- Уведомление вводит в заблуждение говоря об успешной оплате, хотя платеж отклонен https://github.com/dadosha/Java_Auto_Course/issues/2
- Система принимает несуществующей месяц https://github.com/dadosha/Java_Auto_Course/issues/4
- Нет валидации на вводимые значения для поля https://github.com/dadosha/Java_Auto_Course/issues/5
- Проблемы с оформлением
  - https://github.com/dadosha/Java_Auto_Course/issues/3
  - https://github.com/dadosha/Java_Auto_Course/issues/6
  - https://github.com/dadosha/Java_Auto_Course/issues/7
- Нет валидации для вводимых значений для АПИ
  - https://github.com/dadosha/Java_Auto_Course/issues/12
  - https://github.com/dadosha/Java_Auto_Course/issues/10
- Неверные значения принимаются без ошибок для АПИ
  - https://github.com/dadosha/Java_Auto_Course/issues/9
  - https://github.com/dadosha/Java_Auto_Course/issues/8
- Ошибка сервера при неверном заполнение полей в АПИ https://github.com/dadosha/Java_Auto_Course/issues/8
## Количество тест-кейсов
Более 50 кейсов, для кредитной и дебетовой карты повторяются проверки + некоторые кейсы состоят из чек-листов того,
что нужно проверить для поля
## Процент успешных и не успешных тест-кейсов - 50%
## Общие рекомендации
Поправить баги связанные с проверкой и обработкой данных и можно в прод)