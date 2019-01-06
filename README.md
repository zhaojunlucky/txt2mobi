# txt2mobi - A tool convert TXT to kindle mobi files

## How to build
Run `gradle fatJar`

## How to use
* Copy `chapters-setting.txt` into App Jar root directory  
  > You can add your own chapters pattern into it

* Download [kindlegen](https://www.amazon.com/gp/feature.html?ie=UTF8&docId=1000765211) into sub directory kindlegen
* Run `java -jar <jar> <txt file>`
