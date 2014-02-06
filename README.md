
[Android에서 @Inject, @Test](http://helloworld.naver.com/helloworld/342818)

위 글을 읽고서 Android Annotations와 Roboletric를 써봐야 겠다는 생각이 들었다.  
[Android Annotations](http://androidannotations.org/)는 Spring처럼 DI를 제공해 안드로이드 개발이 무척 편해질 것 같았고,  
[Roboletric](http://robolectric.org/)은 안드로이드 UI 테스트를 가능하게 해줄 것 같았다.

그래서 Android + Eclipse + Maven + Android Annotations + Robolectric 환경 구성을 시작하게 되었다.  
사실 Maven은 고려 대상이 아니었으나 Robolectric을 사용하려면 아무래도 Maven을 써야 쉽게 할 수 있을 것 같았다.  
그래서 Maven을 사용하지 않는 방법을 많이 살펴보지는 못했다.

아무튼 거의 하루 정도 삽질을 한 결과 어느 정도 돌아가는 환경을 구성할 수 있었다.  
하지만 아름답지 못하다. 이렇게 해서 이걸 써야 하나 싶은 생각이 들 정도 ㅋ  
Android + Maven + Eclipse 에서 한번 좌절했고,  
([maven-android-plugin](https://code.google.com/p/maven-android-plugin/)이라는 것을 이용한다.)  
Robolectric 에서 한번 또 좌절했다.  
대부분, 특히 Robolectric, 문서도 부실하다.

우선 내 OS 환경은 Mac OS X Maverics 이며,  
이클립스는 Android Developer Tools, Build: v22.3.0-887826 을 사용하고 있다.

## 1. Android SDK


ADT를 사용한다면 이미 Android SDK는 설치되어 있을 것이다.  
다음 두 가지 사항을 체크하자.

- 환경 변수 등록

.profile, .bashrc, .zshrc 등의 파일에 다음 내용을 등록한다.

    export ANDROID_HOME=안드로이드_SDK_설치_경로
    PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

나는 zsh을 쓰기 때문에 .zshrc 파일 끝에 다음 내용을 추가했다.


    export ANDROID_HOME=~/Applications/Android/sdk
    PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

- Android SDK Manager에서 Target 플랫폼 및 Build-tools 설치

4.4.2 버전을 사용하기 때문에 Android 4.4.2 (API 19)와 Android SDK Build-tools (19.0.1)를 설치한다.



## 2. Maven 3.11 설치

android-maven-plugin으로 디바이스에 디플로이하려면 Maven 3.11 버전(혹은 이상)을 사용해야 한다.
Maven을 설치하는 데에는 두 가지 방법이 있다.

- Homebrew

Homebrew를 사용하고 있다면..

    $ brew install maven

- 직접 설치

다음 URL에서 Maven 3.11 binary를 다운로드한다.



[http://maven.apache.org/download.cgi](http://maven.apache.org/download.cgi)

다운이 완료되면 압축 파일을 적당한 곳에 압축해제한다.
앞에서 처럼 환경변수에 등록한다.

    export MVN_HOME=Maven_압축_해제_경로
    PATH=$PATH:$MVN_HOME/bin


## 3. 이클립스 플러그인 설치

설치해야할 이클립스 플러그인과 그 업데이트 사이트는 다음과 같다.  
이클립스에서 모두 설치해주자.

- m2e(m2eclipse): [http://download.eclipse.org/technology/m2e/releases](http://download.eclipse.org/technology/m2e/releases)

  - m2e - Maven integration fror Eclipse
  - m2e - slf4j over logback logging (Optional)

- m2e-android: [http://rgladwell.github.com/m2e-android/updates/](http://rgladwell.github.com/m2e-android/updates/)

  - Android Connector for M2E


- m2e-apt: [http://download.jboss.org/jbosstools/updates/m2e-extensions/m2e-apt](http://download.jboss.org/jbosstools/updates/m2e-extensions/m2e-apt)

  - Maven Integration for Eclipse JDT APT

이 플러그인들에 대해 자세히 알고 싶으면 다음 링크들을 참조하자.

- [http://www.eclipse.org/m2e/](http://www.eclipse.org/m2e/)
- [http://rgladwell.github.io/m2e-android/](http://rgladwell.github.io/m2e-android/)
- [https://github.com/jbosstools/m2e-apt](https://github.com/jbosstools/m2e-apt)

## 4. 안드로이드 라이브러리를 메이븐 로컬 리포지토리로 복사

Maven 중앙 리포지토리에 안드로이드 라이브러리가 4.1.1.4까지만 올라와있다. [참고](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.google.android%22%20AND%20a%3A%22android%22)

그래서 현재 설치된 Android SDK의 라이브러리를 메이븐 로컬 리포지토리로 복사해야 한다.  
다행히도 수동으로 하지 않아도 된다. 다음 URL에서 제공하는 툴을 이용하자.  
(지금 이 말이 이해되지 않아도 상관없다. 그러려니 하고 넘어가면 된다. ㅋ)

[https://github.com/mosabua/maven-android-sdk-deployer](https://github.com/mosabua/maven-android-sdk-deployer)

다음과 같이 실행한다.

  $ git clone git@github.com:mosabua/maven-android-sdk-deployer.git
  $ cd maven-android-sdk-deployer
  $ mvn install -P 4.4

## 5. 이클립스 메이븐 관련 설정

이클립스의 환경 설정 창에서 다음 두 가지를 설정한다.

- Maven > Annotation Processing

  'Automatically configure JDT APT’ 선택

- Maven > Installations

  m2e 플러그인에 기본 포함된 Maven 버전은 3.0.4인데  
  android-maven-plugin를 이용해 빌드하고 디바이스에 배포하려면 Maven 3.1.1 버전이 필요하다.  
  Add… 버튼을 클릭해 앞에서 설치했던 Maven 위치를 지정해 3.1.1 버전을 사용하도록 하자.

## 6. 소스 Clone 및 Import

이제 드디어 소스를 Clone해 Import할 수 있다. ㅎ

  $ git clone git@github.com:kjunine/android-sample.git

이제 이클립스에서 Import해서 메이븐 프로젝트로 변환하거나,  
아니면 바로 메이븐 프로젝트로 Import한다.

제대로 되었다면 pom.xml 파일의 android-maven-plugin 부분에서 에러가 날 것이다.  
에러가 나는데 잘 되었다니 무슨 소린가 하겠지만 에러 표시는 나지만 동작하는 데에 아무 문제가 없다.  
android-maven-plugin 3.7.0 버전을 사용하면 에러가 안 나지만 대신 디바이스에 디플로이를 할 수 없다. ㅋ  
(내가 미리 말하지 않았나 아름답지 않다고.. ㅠㅠ)

## 7. 테스트 실행

JUnit 테스트는 이클립스에서 바로 실행할 수 있다.  
하지만 반드시 Android JUnit Test Launcher가 아닌 Eclipse JUnit Launcher로 실행해야 한다.

## 8. 디바이스에서 실행

일반적인 안드로이드 개발과 달리 디바이스에서 실행하기 위하여  
Run As >Android Application 로 실행하면 안 된다.

Run As > maven build… 으로 실행해야 한다.  
그리고 설정 창에서 다음 사항을 입력 혹은 체크해야 한다.

- Main > Goals: package android:deploy android:run

- Main > Skip Tests: 체크

- Environment > New…

  Name: ANDROID_HOME  
  Value: 안드로이드_SDK_설치_경로(무조건 절대 경로로 입력할 것, ‘~' 사용 안 됨)

처음 한번만 입력해두면 그 다음부터는 툴바의 실행 항목을 이용해 바로 실행할 수 있다.

끝

## POM

참고로 Maven POM 파일을 첨부한다.

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>net.kjunine.sample</groupId>
  <artifactId>android-sample</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>apk</packaging>
  <name>Android Sample</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.6</java.version>
    <android.platform>19</android.platform>
    <android.version>4.4.2_r2</android.version>
    <androidannotations.version>3.0.1</androidannotations.version>
    <spring-android-rest-template.version>1.0.1.RELEASE</spring-android-rest-template.version>
    <robolectric.version>2.2</robolectric.version>
    <junit.version>4.11</junit.version>
    <mockito.version>1.9.5</mockito.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>android</groupId>
      <artifactId>android</artifactId>
      <version>${android.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.androidannotations</groupId>
      <artifactId>androidannotations</artifactId>
      <version>${androidannotations.version}</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.androidannotations</groupId>
      <artifactId>androidannotations-api</artifactId>
      <version>${androidannotations.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework.android</groupId>
      <artifactId>spring-android-rest-template</artifactId>
      <version>${spring-android-rest-template.version}</version>
    </dependency>
    <dependency>
      <groupId>org.robolectric</groupId>
      <artifactId>robolectric</artifactId>
      <version>${robolectric.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <version>${mockito.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <directory>${project.basedir}/bin</directory>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.5.1</version>
        <configuration>
          <source>${java.version}</source>
          <target>${java.version}</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>com.jayway.maven.plugins.android.generation2</groupId>
        <artifactId>android-maven-plugin</artifactId>
        <version>3.8.0</version>
        <extensions>true</extensions>
        <configuration>
          <sdk>
            <platform>${android.platform}</platform>
          </sdk>
          <proguard>
            <skip>true</skip>
          </proguard>
          <undeployBeforeDeploy>true</undeployBeforeDeploy>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

## LICENSE

The MIT License (MIT)

Copyright (c) 2013 Daniel Ku kjunine@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
