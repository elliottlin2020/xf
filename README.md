# 项目名称：Dashboard

本项目是基于 JHipster 7.0.1,使用 Pro701 蓝图生成，您可以在相应位置找到文档和帮助：[https://www.jhipster.tech/documentation-archive/v7.0.1](https://www.jhipster.tech/documentation-archive/v7.0.1) ， Pro701 蓝图参考文档：[JHipster.Pro](http://www.jhipster.pro/) 。

## 开发说明

要以开发配置（dev profile）启动您的应用程序，请运行：

```
./mvnw
```

有关如何使用 JHipster 进行开发的更多说明，请参阅在开发中使用[Using JHipster in development][]。

## 系统构建

### 打包为 Jar 包

要构建最终的 Jar 包并优化用于生产的 Dashboard 应用程序，请运行：

```
./mvnw -Pprod clean verify
```

上面过程中可能会因为测试失败而无法完成，请暂时忽略测试再运行，测试代码正在整理中。：

```
./mvnw -Pprod clean verify -DskipTests
```

为确保一切正常，请运行：

```
java -jar target/*.jar
```

有关更多详细信息，请参阅在生产中使用[Using JHipster in production][]。

### 打包为 War 包

要将应用程序打包为 War 包以将其部署到应用程序服务器，请运行：

```
./mvnw -Pprod,war clean verify
```

上面过程中可能会因为测试失败而无法完成，请暂时忽略测试再运行，测试代码正在整理中。

```
./mvnw -Pprod,war clean verify -DskipTests
```

## 测试

要启动应用程序的测试，请运行：

```
./mvnw verify
```

有关更多信息，请参阅[Running tests page][]。

### 代码质量

Sonar 用于分析代码质量。 您可以使用以下命令启动本地 Sonar 服务器（可从 http://localhost:9001 访问）：

```
docker-compose -f src/main/docker/sonar.yml up -d
```

注意：我们在尝试 SonarQube 时已关闭[src/main/docker/sonar.yml](src/main/docker/sonar.yml)中的身份验证以提供开箱即用的体验，对于实际用例，请重新打开它。

您可以使用[sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner)或使用 maven pluginmaven 插件运行 Sonar 分析。

然后，运行 Sonar 分析:

```
./mvnw -Pprod clean verify sonar:sonar
```

如果您需要重新运行 Sonar 阶段，请确保至少指定`initialize`阶段，因为 Sonar 属性是从 sonar-project.properties 文件加载的。

```
./mvnw initialize sonar:sonar
```

有关更多信息，请参阅[Code quality page][]。

## 使用 Docker 简化开发（可选）

您可以使用 Docker 来改善您的 JHipster 开发体验。 [src/main/docker](src/main/docker)文件夹中提供了许多 docker-compose 配置，以启动所需的第三方服务。

例如，要在 Docker 容器中启动 mysql 数据库，请运行：

```
docker-compose -f src/main/docker/mysql.yml up -d
```

要停止并删除容器，请运行：

```
docker-compose -f src/main/docker/mysql.yml down
```

您还可以对应用程序及其依赖的所有服务进行完全 docker 化。
为此，请首先通过运行以下操作为您的应用构建 docker 映像：

```
./mvnw -Pprod verify jib:dockerBuild
```

然后运行：

```
docker-compose -f src/main/docker/app.yml up -d
```

有关更多信息，请参阅[Using Docker and Docker-Compose][]，此页面还包含有关 docker-compose 子生成器（`jhipster docker-compose`）的信息，该子生成器能够为一个或多个 JHipster 应用程序生成 docker 配置。

## 持续集成（可选）

要为您的项目配置 CI，请运行 ci-cd 子生成器（`jhipster ci-cd`），这将使您为许多 Continuous Integration 系统生成配置文件。 有关更多信息，请查阅[Setting up Continuous Integration][]页面。

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.0.1 archive]: https://www.jhipster.tech/documentation-archive/v7.0.1
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.0.1/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.0.1/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.0.1/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.0.1/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.0.1/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.0.1/setting-up-ci/
