# Проект "Обмен валют"
REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, и совершать расчёт конвертации произвольных сумм из одной валюты в другую.
Фреймворки не используются

## База данных
В качестве базы данных была использована SQLite.

### Таблица "Currencies"
| Колонка  | Тип     | Комментарий                                |
|----------|---------|--------------------------------------------|
| Id       | INTEGER | Айди валюты, первичный ключ, автоинкремент |
| Code     | VARCHAR | Код валюты, уникальный                     |
| Name     | VARCHAR | Полное имя валюты                          |
| Sign     | VARCHAR | Символ валюты                              |

Пример записи в таблице для американского доллара:

| ID  | Code | FullName          | Sign |
|-----|------|-------------------|------|
| 1   | USD  | US Dollar         |  $   |

Список валют был взят с [https://www.iban.com/currency-codes]

### Таблица "ExchangeRates"

| Колонка          | Тип        | Комментарий                                                 |
|------------------|------------|-------------------------------------------------------------|
| Id               | INTEGER    | Айди курса обмена, автоинкремент, первичный ключ            |
| BaseCurrencyId   | INTEGER    | ID базовой валюты,  внешний ключ на Currencies.Id           |
| TargetCurrencyId | INTEGER    | ID целевой валюты,  внешний ключ на Currencies.Id           |
| Rate             | DECIMAL    | Курс обмена единицы базовой валюты к единице целевой валюты |

Пример записи в таблице:
| Id  | BaseCurrencyId | TargetCurrencyId | Rate |
|-----|----------------|------------------|------|
| 1   |       1        |        4         | 0.91 |

### Запросы
#### GET /currencies
Получение списка валют. Пример ответа:
```
[
    {
        "id": 1,
        "code": "USD"
        "name": "US Dollar",
        "sign": "$"
    },   
    {
        "id": 2,
        "code": "EUR",
        "name": "Euro",
        "sign": "€"
    }
]
```

#### GET /currency/EUR
Получение конкретной валюты. Пример ответа:
```
{
    "id": 2,
    "code": "EUR",
    "name": "Euro",
    "sign": "€"
}
```
#### POST /currencies
Добавление новой валюты в базу. Данные передаются в теле запроса в виде полей формы (`x-www-form-urlencoded`). Поля формы - "name", "code", "sign".

#### GET /exchangeRates
Получение списка всех обменных курсов. Пример ответа:
```
[
    {
        "id": 1,
        "baseCurrency": {
            "id": 1,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 2,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.98
    }
]
```

#### GET /exchangeRate/USDRUB
Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. Пример ответа:
```
{
    "id": 3,
    "baseCurrency": {
        "id": 1,
        "code": "USD"
        "name": "US Dollar",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 3,
        "code": "RUB",
        "name": "Russian Ruble",
        "sign": "₽"
    },
    "rate": 94.68
}
```

#### POST /exchangeRates

Добавление нового обменного курса в базу. Данные передаются в теле запроса в виде полей формы (`x-www-form-urlencoded`). Поля формы - `baseCurrencyCode`, `targetCurrencyCode`, `rate`. Пример полей формы:
- "baseCurrencyCode" - USD
- "targetCurrencyCode" - EUR
- "rate" - 0.99

#### GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT
Расчёт перевода определённого количества средств из одной валюты в другую. Пример запроса - GET /exchange?from=USD&to=RUB&amount=10.
Пример ответа:
```
{
    "baseCurrency": {
        "id": 1,
        "code": "USD",
        "name": "US Dollar",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 3,
        "code": "RUB",
        "name": "Russian Ruble",
        "sign": "A€"
    },
    "rate": 94.68,
    "amount": 10.00
    "convertedAmount": 946.8
}
```
