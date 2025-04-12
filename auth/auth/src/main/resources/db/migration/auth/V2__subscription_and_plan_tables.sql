-- Создание таблицы с тарифами подписки
CREATE TABLE subscription_plan (
       id               SERIAL PRIMARY KEY,
       name             VARCHAR(50) NOT NULL UNIQUE,     -- Название: FREE, PLUS, PRO и т.п.
       price            NUMERIC(10, 2) NOT NULL DEFAULT 0.0,
       duration_days    INT NOT NULL,                     -- Срок подписки в днях
       description      TEXT
);

-- Создание таблицы с подписками клиентов
CREATE TABLE subscription (
      id                BIGSERIAL PRIMARY KEY,
      client_id         BIGINT NOT NULL REFERENCES client(id) ON DELETE CASCADE,
      plan_id           INT NOT NULL REFERENCES subscription_plan(id),
      start_date        TIMESTAMP NOT NULL DEFAULT now(),
      end_date          TIMESTAMP,
      is_active         BOOLEAN NOT NULL DEFAULT true,
      canceled_at       TIMESTAMP
);

-- Уникальная активная подписка на клиента
CREATE UNIQUE INDEX uq_active_subscription_per_client
    ON subscription (client_id)
    WHERE is_active = true;

-- Наполнение таблицы тарифов дефолтными значениями
INSERT INTO subscription_plan(name, price, duration_days, description)
VALUES
    ('FREE', 0.00, 0, 'Бесплатный базовый тариф'),
    ('PLUS', 9.99, 30, 'Платный тариф с дополнительными функциями'),
    ('PRO', 19.99, 30, 'Полный доступ ко всем функциям');
