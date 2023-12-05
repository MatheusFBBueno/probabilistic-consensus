Instruções para Instalação
Instalar Java 17.0.6-zulu
curl -s "https://get.sdkman.io" | bash
source ~/.sdkman/bin/sdkman-init.sh
sdk install java 17.0.6-zulu

Instalar Maven 3.8.7
sdk install maven 3.8.7

Instalação clean do Maven
mvn clean install

Buildar projeto (executar do diretorio base do projeto t1-distribuida)
mvn clean package

Executar teste local
Abrir projeto no seu editor (IntelliJ, Eclipse, etc) e executar a class org.example.Main
