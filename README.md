# Телеграмм бот для торговли в режиме песочницы Тинькофф Инвестиции

Авторы: 
* Шумилин Илья
* Нечуговских Антон
* Казанцев Артём

##Как развернуть бота на сервере

### Подготавливаем окружение
```
$ sudo apt update
$ sudo apt upgrade
```

#### Устанавливаем git
```
$ sudo apt install git
$ git --version
git version 2.25.1
```

#### Устанавливаем jdk
```
$ sudo apt install openjdk-11-jdk
$ java --version
openjdk 11.0.9.1 2020-11-04
```

#### Устанавливаем maven
```
$ sudo apt install maven
$ mvn --version
Apache Maven 3.6.3
```

#### Клонируем [репозиторий](https://github.com/TonyCardio/TinkoffInvestSandboxBot) с гитхаба
```
$ git clone https://github.com/TonyCardio/TinkoffInvestSandboxBot
```

#### Переходим в папку с проектом и собираем решение
```
$ cd TinkoffInvestSandboxBot
$ mvn install
```
__В папке с проектом должна появиться папка target__
#### Перед запуском добавим token бота в переменные среды
```
$ nano etc/environment

...
JAVA_BOT_TOKEN="<токен>"
...

```
#### Запуск
__Если ваша папка target выглядит как-то так, то вы в шаге от победы__
```
~/TinkoffInvestSandboxBot/target$ ls
TinkoffInvestSandboxBot-1.0-SNAPSHOT-jar-with-dependencies.jar
TinkoffInvestSandboxBot-1.0-SNAPSHOT.jar
archive-tmp
classes
generated-sources
generated-test-sources
maven-archiver
maven-status
surefire-reports
test-classes
```
__Запускаем__
```
$ java -jar TinkoffInvestSandboxBot-1.0-SNAPSHOT-jar-with-dependencies.jar
```